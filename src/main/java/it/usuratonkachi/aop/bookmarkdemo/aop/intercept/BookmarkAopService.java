package it.usuratonkachi.aop.bookmarkdemo.aop.intercept;

import it.usuratonkachi.aop.bookmarkdemo.aop.annotation.Bookmarkable;
import it.usuratonkachi.aop.bookmarkdemo.bookmark.Bookmark;
import it.usuratonkachi.aop.bookmarkdemo.bookmark.BookmarkError;
import it.usuratonkachi.aop.bookmarkdemo.bookmark.IBookmarkData;
import it.usuratonkachi.aop.bookmarkdemo.bookmark.service.BookmarkService;
import it.usuratonkachi.aop.bookmarkdemo.context.BookmarkStatus;
import it.usuratonkachi.aop.bookmarkdemo.context.Envelope;
import it.usuratonkachi.aop.bookmarkdemo.exception.BlockingException;
import it.usuratonkachi.aop.bookmarkdemo.exception.BookmarkException;
import it.usuratonkachi.aop.bookmarkdemo.exception.RetryableException;
import it.usuratonkachi.aop.bookmarkdemo.utils.BookmarkUtils;
import it.usuratonkachi.aop.bookmarkdemo.utils.ReflectionUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Aspect
@Service
@Order(1)
@RequiredArgsConstructor
public class BookmarkAopService<T extends IBookmarkData<T>> {

    @Value("${bookmark.maxRetry}")
    private Integer maxRetry;
    private final BookmarkService<T> bookmarkService;
    private final List<BookmarkStatus> acceptedBookmarkStatus = List.of(BookmarkStatus.INCOMPLETE, BookmarkStatus.TODO);

    @Pointcut("@annotation(it.usuratonkachi.aop.bookmarkdemo.aop.annotation.Bookmarkable)")
    public void bookmarkableMethod() {}

    /**
     * Bookmark retrieve (optional in some cases)
     * Basic check for completition
     * Custom checks for each step
     * Custom alter of wrapper object, for fallback and retries
     * Update And Save Bookmark
     * Return result
     *
     * @param joinPoint
     * @return
     * @throws Throwable
     */
    @Around("bookmarkableMethod()")
    @SuppressWarnings("unchecked")
    public Object bookmarkMethod(ProceedingJoinPoint joinPoint) throws Throwable {
        Bookmarkable bookmarkable = BookmarkUtils.getBookmarkAnnotation(joinPoint);
        Class<T> dataType = (Class<T>) bookmarkable.bookmarkJavaType();
        AtomicInteger retries = new AtomicInteger(0);

        return Mono.justOrEmpty(Optional.ofNullable(joinPoint.getArgs()))
                .filter(args -> args.length > 0)
                .map(args -> args[0])
                .filter(obj -> obj instanceof Envelope)
                .map(Envelope.class::cast)
                .flatMap(envelope ->
                        // For logging purpose, use clear method name
                        Mono.justOrEmpty(bookmarkService.getBookmark(envelope, dataType))
                                // UPDATE ENVELOPE IF BOOK IS PRESENT
                                .map(bookmark -> bookmark.updateEnvelope(envelope))
                                // ELSE USE BOOKMARK FROM ENVELOPE
                                .switchIfEmpty(Mono.defer(() -> Mono.just((Bookmark<T>)envelope.getBookmark(dataType.getName()))))
                                // CHECK IF BOOKMARK STEP IS TO DO OR TO RETRY
                                .filter(bookmark -> {
                                    //
                                    BookmarkError bookmarkError;
                                    BookmarkException bookmarkException;
                                    if (acceptedBookmarkStatus.contains(bookmark.getBookmarkStatus())) {
                                        return true;
                                    } else if (BookmarkStatus.ERROR.equals(bookmark.getBookmarkStatus())
                                            && bookmark.getError() != null
                                            && bookmark.getError().getException() instanceof RetryableException
                                            && ((RetryableException)bookmark.getError().getException()).getRetry() <= maxRetry) {
                                        retries.set(((RetryableException)bookmark.getError().getException()).getRetry());
                                        return true;
                                    } else {
                                        log.warn(String.format("Step %s already managed, for message id %s skipping step", dataType.getName(), envelope.getId()));
                                        return false;
                                    }
                                })
                                // CALL METHOD
                                .flatMap(bookmark ->
                                        ReflectionUtils.methodCall(joinPoint)
                                                // UPDATE BOOKMARK
                                                .doOnNext(bookmark::updateBookmark)
                                                // SAVE BOOKMARK
                                                .doOnNext(result -> bookmarkService.saveBookmark(result, bookmark))
                                                .doOnNext(result -> {
                                                    if (result.getBookmarkDataMap().values().stream().map(Bookmark::getBookmarkStatus).allMatch(BookmarkStatus.DONE::equals))
                                                        bookmarkService.deleteBookmarks(result, bookmark);
                                                })
                                                // ERROR MANAGEMENT, WE HAVE BOOKMARK HERE
                                                .doOnError(RetryableException.class, e -> e.setRetry(retries.get()))
                                                .onErrorMap(e -> e instanceof BookmarkException ? e : new BlockingException(e))
                                                .doOnError(BookmarkException.class, e -> bookmarkService.saveBookmark(envelope, bookmark))
                                )
                                .switchIfEmpty(Mono.defer(() -> Mono.just(envelope)))
                                // ERROR MANAGEMENT, WE HAVE TO RECREATE BOOKMARK HERE
                                .doOnError(RetryableException.class, e -> e.setRetry(retries.get()))
                                .onErrorMap(e -> e instanceof BookmarkException ? e : new BlockingException(e))
                                .doOnError(BookmarkException.class, e -> {
                                    Bookmark<T> bookmark = (Bookmark<T>) ReflectionUtils.createErrorBookmark(dataType, e);
                                    bookmarkService.saveBookmark(envelope, bookmark);
                                })
                );
    }

}

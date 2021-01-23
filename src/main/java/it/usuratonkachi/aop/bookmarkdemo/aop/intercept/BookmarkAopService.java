package it.usuratonkachi.aop.bookmarkdemo.aop.intercept;

import it.usuratonkachi.aop.bookmarkdemo.aop.annotation.Bookmarkable;
import it.usuratonkachi.aop.bookmarkdemo.bookmark.Bookmark;
import it.usuratonkachi.aop.bookmarkdemo.bookmark.IBookmarkData;
import it.usuratonkachi.aop.bookmarkdemo.bookmark.service.BookmarkService;
import it.usuratonkachi.aop.bookmarkdemo.bookmark.step.Step1_Filtering;
import it.usuratonkachi.aop.bookmarkdemo.bookmark.step.Step2_Date;
import it.usuratonkachi.aop.bookmarkdemo.bookmark.step.Step3_Object;
import it.usuratonkachi.aop.bookmarkdemo.context.BookmarkStatus;
import it.usuratonkachi.aop.bookmarkdemo.context.Envelope;
import it.usuratonkachi.aop.bookmarkdemo.exception.BlockingException;
import it.usuratonkachi.aop.bookmarkdemo.exception.BookmarkException;
import it.usuratonkachi.aop.bookmarkdemo.exception.RetrievableException;
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
import java.util.Map;
import java.util.Optional;

@Slf4j
@Aspect
@Service
@Order(1)
@RequiredArgsConstructor
public class BookmarkAopService<T extends IBookmarkData<T>> {

    @Value("${bookmark.maxRetry}")
    private Integer maxRetry;

    private final BookmarkService<T> bookmarkService;

    private final List<BookmarkStatus> acceptedStatus = List.of(BookmarkStatus.TODO, BookmarkStatus.INCOMPLETE, BookmarkStatus.ERROR_RETRYING);

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
        String bookmarkName = BookmarkUtils.getBookmarkName(joinPoint);
        Bookmarkable bookmarkable = BookmarkUtils.getBookmarkAnnnotation(joinPoint);
        Class<?> dataType = bookmarkable.bookmarkJavaType();

        return Mono.justOrEmpty(Optional.ofNullable(joinPoint.getArgs()))
                .filter(args -> args.length > 0)
                .map(args -> args[0])
                .filter(obj -> obj instanceof Envelope)
                .map(Envelope.class::cast)
                .flatMap(envelope ->
                        // For logging purpose, use clear method name
                        Mono.justOrEmpty(bookmarkService.getBookmark(envelope))
                                // UPDATE ENVELOPE IF BOOK IS PRESENT
                                .map(bookmark -> bookmark.updateEnvelope(envelope))
                                // ELSE USE BOOKMARK FROM ENVELOPE
                                .switchIfEmpty(Mono.defer(() -> Mono.just((Bookmark<T>)envelope.getBookmark(dataType.getName()))))
                                // CHECK IF BOOKMARK STEP IS TO DO OR TO RETRY
                                .filter(bookmark -> acceptedStatus.contains(bookmark.getBookmarkStatus()))
                                // CALL METHOD
                                .flatMap(bookmark ->
                                        ReflectionUtils.methodCall(joinPoint)
                                                // UPDATE BOOKMARK
                                                .doOnNext(bookmark::updateBookmark)
                                                // SAVE BOOKMARK
                                                .doOnNext(result -> bookmarkService.saveBookmark(result, bookmark))
                                )
                                .switchIfEmpty(Mono.defer(() -> Mono.just(envelope)))
                                .doOnError(BookmarkException.class, e -> {
                                    // TODO CREATE BOOKMARK WITH ERROR!
                                    e.printStackTrace();
                                    //bookmarkService.saveBookmark(envelope, bookmarkName, dataType, e)
                                })
                );
    }

}

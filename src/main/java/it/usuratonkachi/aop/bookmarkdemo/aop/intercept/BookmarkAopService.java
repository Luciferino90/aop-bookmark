package it.usuratonkachi.aop.bookmarkdemo.aop.intercept;

import it.usuratonkachi.aop.bookmarkdemo.aop.annotation.Bookmarkable;
import it.usuratonkachi.aop.bookmarkdemo.bookmark.Bookmark;
import it.usuratonkachi.aop.bookmarkdemo.bookmark.IBookmarkData;
import it.usuratonkachi.aop.bookmarkdemo.bookmark.service.BookmarkService;
import it.usuratonkachi.aop.bookmarkdemo.bookmark.step.Step1_Filtering;
import it.usuratonkachi.aop.bookmarkdemo.bookmark.step.Step2_Modifying;
import it.usuratonkachi.aop.bookmarkdemo.bookmark.step.Step3_FetchResultAndSave;
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

import java.util.Map;
import java.util.Optional;

@Slf4j
@Aspect
@Service
@Order(1)
@RequiredArgsConstructor
public class BookmarkAopService {

    @Value("${bookmark.maxRetry}")
    private Integer maxRetry;

    private final BookmarkService bookmarkService;

    private Map<Class<?>, Integer> orderMap = Map.of(
            Step1_Filtering.class, 0,
            Step2_Modifying.class, 1,
            Step3_FetchResultAndSave.class, 2
    );

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
    public Object bookmarkMethod(ProceedingJoinPoint joinPoint) throws Throwable {
        String bookmarkName = BookmarkUtils.getBookmarkName(joinPoint);
        Bookmarkable bookmarkable = BookmarkUtils.getBookmarkAnnnotation(joinPoint);
        Class<?> dataType = bookmarkable.bookmarkJavaType();

        return Mono.justOrEmpty(Optional.ofNullable(joinPoint.getArgs()))
                .filter(args -> args.length > 0)
                .map(args -> args[0])
                .filter(obj -> obj instanceof Envelope)
                .map(Envelope.class::cast)
                .flatMap(wrapperContext ->
                        // For logging purpose, use clear method name
                        Mono.justOrEmpty(bookmarkService.getBookmark(wrapperContext))
                                // TODO ALLIGN ENVELOPE WITH BOOKMARK -> USARE + FILE
                                .filter(bookmark -> isNotBlockingError(bookmark, bookmarkName))
                                .switchIfEmpty(Mono.defer(() -> {
                                    IBookmarkData bookmarkData = (IBookmarkData) ReflectionUtils.createInstance(dataType, wrapperContext);
                                    wrapperContext.setBookmarkData(bookmarkData);
                                    return Mono.justOrEmpty(BookmarkUtils.generateBookmark(wrapperContext, bookmarkName, dataType, null));
                                }))
                                .filter(bookmark -> isRightStepOrder(bookmark, dataType, bookmarkName))
                                /*.map(bookmark -> {
                                    if (bookmark instanceof IAlteringAndFilteringBookmarkData) {
                                        envelope.setAlreadyDeliveryRcptTo(bookmark.getAlreadyDeliveryRcptTo());
                                        ((IAlteringAndFilteringBookmarkData) bookmark).alter(wrapperContext);
                                    }
                                    return bookmark;
                                })*/
                                .flatMap(bookmark ->
                                    ReflectionUtils.methodCall(joinPoint)
                                        .doOnNext(bookmark::updateBookmark)
                                        .doOnNext(result -> bookmarkService.saveBookmark(result, bookmarkName, dataType, null))
                                )
                                // Nonsense, checked notnull condition at 42-44
                                .switchIfEmpty(Mono.defer(() -> Mono.just(wrapperContext)))
                                .doOnError(BookmarkException.class, e -> bookmarkService.saveBookmark(wrapperContext, bookmarkName, dataType, e))
                );
    }

    public boolean isRightStepOrder(Bookmark bookmark, Class<?> actualDatatypeAnnotation, String bookmarkName) {
        if (orderMap.get(bookmark.getData().getClass()) > orderMap.get(actualDatatypeAnnotation)) {
            log.info(bookmarkName + ": Skipped for WrongOrder");
            return false;
        }
        return true;
    }

    private boolean isRightStep(String bookmarkName, Bookmark bookmark) {
        if (!bookmarkName.equalsIgnoreCase(bookmark.getMeta().getBookmarkName())) {
            log.info(bookmarkName + ": Skipped for WrongOrder");
            return false;
        }
        return true;
    }

    private boolean isNotBlockingError(Bookmark bookmark, String bookmarkName){
        boolean isBlockingError = false;
        if (bookmark.getError() == null)
            return !isBlockingError;
        BookmarkException bookmarkException = bookmark.getError().getException();
        if (bookmarkException instanceof BlockingException)
            isBlockingError = true;
        if (bookmarkException instanceof RetrievableException) {
            isBlockingError = ((RetrievableException)bookmarkException).getRetry() >= maxRetry;
        }
        if (isBlockingError) {
            log.info(bookmarkName + ": Skipped for BlockingError");
            return !isBlockingError;
        }
        return !isBlockingError;
    }

    private boolean checkBookmarkFilter(Bookmark bookmark, Envelope wrapperContext, String bookmarkName) {
        if (!bookmark.filter(wrapperContext))  {
            log.info(bookmarkName + ": Skipped for Bookmark filter");
            return false;
        }
        return true;
    }

}

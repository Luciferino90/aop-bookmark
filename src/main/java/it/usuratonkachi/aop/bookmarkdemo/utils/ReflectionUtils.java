package it.usuratonkachi.aop.bookmarkdemo.utils;

import it.usuratonkachi.aop.bookmarkdemo.bookmark.Bookmark;
import it.usuratonkachi.aop.bookmarkdemo.bookmark.BookmarkError;
import it.usuratonkachi.aop.bookmarkdemo.bookmark.IBookmarkData;
import it.usuratonkachi.aop.bookmarkdemo.context.BookmarkStatus;
import it.usuratonkachi.aop.bookmarkdemo.context.Envelope;
import it.usuratonkachi.aop.bookmarkdemo.exception.BookmarkException;
import lombok.experimental.UtilityClass;
import org.aspectj.lang.ProceedingJoinPoint;
import reactor.core.publisher.Mono;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@UtilityClass
public class ReflectionUtils {

    @SuppressWarnings("unchecked")
    public Mono<Envelope> methodCall(ProceedingJoinPoint joinPoint) {
        try {
            return ((Mono<Envelope>)joinPoint.proceed());
        } catch (Throwable throwable) {
            if (throwable instanceof RuntimeException)
                throw (RuntimeException)throwable;
            else
                throw new RuntimeException(throwable);
        }
    }

    public Bookmark<? extends IBookmarkData<?>> createErrorBookmark(Class<?> clazz, BookmarkException bookmarkException) {
        try {
            Method method = clazz.getMethod("createBookmarkData");
            Object object = method.invoke(null);
            Bookmark<?> bookmark = (Bookmark<?>) object;
            bookmark.setError(BookmarkError.generateError(bookmarkException));
            bookmark.setBookmarkStatus(BookmarkStatus.ERROR);
            return bookmark;
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

}

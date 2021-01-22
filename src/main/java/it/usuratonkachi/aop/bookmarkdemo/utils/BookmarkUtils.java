package it.usuratonkachi.aop.bookmarkdemo.utils;

import it.usuratonkachi.aop.bookmarkdemo.aop.annotation.Bookmarkable;
import it.usuratonkachi.aop.bookmarkdemo.bookmark.Bookmark;
import it.usuratonkachi.aop.bookmarkdemo.bookmark.BookmarkError;
import it.usuratonkachi.aop.bookmarkdemo.bookmark.Metadata;
import it.usuratonkachi.aop.bookmarkdemo.context.WrapperContext;
import it.usuratonkachi.aop.bookmarkdemo.exception.BookmarkException;
import lombok.experimental.UtilityClass;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Method;

@UtilityClass
public class BookmarkUtils {

    public String getBookmarkName(ProceedingJoinPoint joinPoint) {
        Method method = ((MethodSignature)joinPoint.getSignature()).getMethod();
        String className = method.getDeclaringClass().getSimpleName();
        String methodName = method.getName();
        return className + ":" + methodName;
    }

    public Bookmarkable getBookmarkAnnnotation(ProceedingJoinPoint joinPoint) {
        Method method = ((MethodSignature)joinPoint.getSignature()).getMethod();
        return method.getDeclaredAnnotation(Bookmarkable.class);
    }

    public Bookmark generateBookmark(WrapperContext wrapperContext, String bookmarkName, Class<?> dataType, BookmarkException bookmarkException) {
        return Bookmark.builder()
                .id(wrapperContext.getId())
                .meta(Metadata.builder().bookmarkName(bookmarkName).dataType(dataType.getName()).build())
                .error(BookmarkError.generateError(bookmarkException))
                .data(wrapperContext.getBookmarkData())
                .build();
    }

}

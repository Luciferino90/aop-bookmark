package it.usuratonkachi.aop.bookmarkdemo.utils;

import it.usuratonkachi.aop.bookmarkdemo.aop.annotation.Bookmarkable;
import it.usuratonkachi.aop.bookmarkdemo.bookmark.Bookmark;
import it.usuratonkachi.aop.bookmarkdemo.bookmark.BookmarkError;
import it.usuratonkachi.aop.bookmarkdemo.bookmark.Metadata;
import it.usuratonkachi.aop.bookmarkdemo.context.WrapperContext;
import it.usuratonkachi.aop.bookmarkdemo.exception.BlockingException;
import it.usuratonkachi.aop.bookmarkdemo.exception.BookmarkException;
import lombok.experimental.UtilityClass;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Method;

@UtilityClass
public class ReflectionUtils {

    public Object methodCall(ProceedingJoinPoint joinPoint) {
        try {
            return joinPoint.proceed();
        } catch (Throwable throwable) {
            if (throwable instanceof RuntimeException)
                throw (RuntimeException)throwable;
            else
                throw new RuntimeException(throwable);
        }
    }

    public Object createInstance(Class<?> dataType, WrapperContext wrapperContext) {
        try {
            return dataType.getDeclaredConstructor(wrapperContext.getClass()).newInstance(wrapperContext);
        } catch (Exception e) {
            throw new BlockingException(e);
        }
    }

}

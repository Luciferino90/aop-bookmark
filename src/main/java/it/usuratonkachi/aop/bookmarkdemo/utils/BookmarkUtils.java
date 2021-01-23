package it.usuratonkachi.aop.bookmarkdemo.utils;

import it.usuratonkachi.aop.bookmarkdemo.aop.annotation.Bookmarkable;
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

}

package it.usuratonkachi.aop.bookmarkdemo.utils;

import it.usuratonkachi.aop.bookmarkdemo.context.WrapperContext;
import it.usuratonkachi.aop.bookmarkdemo.exception.BlockingException;
import lombok.experimental.UtilityClass;
import org.aspectj.lang.ProceedingJoinPoint;
import reactor.core.publisher.Mono;

@UtilityClass
public class ReflectionUtils {

    @SuppressWarnings("unchecked")
    public Mono<WrapperContext> methodCall(ProceedingJoinPoint joinPoint) {
        try {
            return ((Mono<WrapperContext>)joinPoint.proceed());
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

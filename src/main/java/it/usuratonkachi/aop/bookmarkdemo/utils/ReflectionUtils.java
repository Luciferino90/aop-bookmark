package it.usuratonkachi.aop.bookmarkdemo.utils;

import it.usuratonkachi.aop.bookmarkdemo.context.Envelope;
import it.usuratonkachi.aop.bookmarkdemo.exception.BlockingException;
import lombok.experimental.UtilityClass;
import org.aspectj.lang.ProceedingJoinPoint;
import reactor.core.publisher.Mono;

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

    public Object createInstance(Class<?> dataType, Envelope wrapperContext) {
        try {
            return dataType.getDeclaredConstructor(wrapperContext.getClass()).newInstance(wrapperContext);
        } catch (Exception e) {
            throw new BlockingException(e);
        }
    }

}

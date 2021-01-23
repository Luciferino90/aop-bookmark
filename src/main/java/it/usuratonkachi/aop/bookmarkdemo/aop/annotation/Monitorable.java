package it.usuratonkachi.aop.bookmarkdemo.aop.annotation;

import java.lang.annotation.*;

/**
 * Annotation for MonitoringAopService
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface Monitorable {
}

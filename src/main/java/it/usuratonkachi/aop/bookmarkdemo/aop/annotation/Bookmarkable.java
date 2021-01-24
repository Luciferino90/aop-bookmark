package it.usuratonkachi.aop.bookmarkdemo.aop.annotation;

import org.springframework.core.annotation.AliasFor;

import java.io.Serializable;
import java.lang.annotation.*;

/**
 * Annotation for BookmarkAopService
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface Bookmarkable {

    @AliasFor("bookmarkJavaType") Class<?> value() default Serializable.class;
    @AliasFor("value") Class<?> bookmarkJavaType() default Serializable.class;

}

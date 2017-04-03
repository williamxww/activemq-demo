package com.bow.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
public @interface Listener {

    /**
     * 消息队列的名称
     * 
     * @return
     */
    String topic() default "";

    /**
     * 是否支持事务,默认不开启
     * 
     * @return
     */
    boolean transaction() default false;

    /**
     * 是否支持n2级别的消息，默认不开启
     * 
     * @return
     */
    boolean n2() default false;
}

package com.bow.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.bow.spring.MessageConsumerConfiguration;
import org.springframework.context.annotation.Import;


@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Import(MessageConsumerConfiguration.class)
public @interface EnableMessageQueue {
}

package com.bow.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.bow.mq.MessageProducerConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableScheduling;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Import(MessageProducerConfiguration.class)
@EnableScheduling
public @interface EnableProducer {
}

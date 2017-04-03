package com.bow.mq;

import javax.annotation.Resource;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan("com.zjp.producer")
public class MessageProducerConfiguration {

    @Resource
    private ProducerConfig producerConfig;

    @Bean
    public ActiveMQConnectionFactory activeMQConnectionFactory() {
        return new ActiveMQConnectionFactory(producerConfig.getBrokerUrl(),
                producerConfig.getUserName(), producerConfig.getPassword());
    }
}

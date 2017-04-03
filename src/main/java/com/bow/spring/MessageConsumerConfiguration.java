package com.bow.spring;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.annotation.Resource;

import com.bow.mq.ConsumerConfig;
import com.bow.mq.RepeatMessageHandler;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan("com.bow")
public class MessageConsumerConfiguration {

    @Bean
    public ConsumerConfig consumerConfig() {
        ConsumerConfig config = new ConsumerConfig();
        config.setBrokerUrl("");
        config.setPoolSize(2);
        config.setUserName("admin");
        config.setPassword("admin");
        return config;
    }

    /**
     * 创建消费者解析器
     *
     * @return
     */
    @Bean
    public MessageConsumerBeanPostProcessor messageConsumerBeanPostProcessor() {
        return new MessageConsumerBeanPostProcessor();
    }

    @Resource
    private ConsumerConfig consumerConfig;

    /**
     * 创建ActiveMQ连接工厂
     *
     * @return
     */
    @Bean
    public ActiveMQConnectionFactory activeMQConnectionFactory() {
        return new ActiveMQConnectionFactory(consumerConfig.getBrokerUrl(), consumerConfig.getUserName(),
                consumerConfig.getPassword());
    }

    /**
     * 创建线程池
     *
     * @return
     */
    @Bean
    public Executor executor() {
        return Executors.newFixedThreadPool(consumerConfig.getPoolSize());
    }

    /**
     * 创建消息幂等处理
     *
     * @return
     */
    @Bean
    public RepeatMessageHandler repeatMessageHandle() {
        return new RepeatMessageHandler();
    }
}
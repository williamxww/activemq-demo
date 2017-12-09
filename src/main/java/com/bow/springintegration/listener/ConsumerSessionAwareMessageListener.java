package com.bow.springintegration.listener;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.listener.SessionAwareMessageListener;

/**
 * 此类为spring提供，非JMS规范提供<br/>
 * 拿到session，可以在收到消息后发送回复消息
 */
public class ConsumerSessionAwareMessageListener implements
        SessionAwareMessageListener<TextMessage> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConsumerSessionAwareMessageListener.class);

    private Destination destination;

    public void onMessage(TextMessage message, Session session) throws JMSException {
        LOGGER.info("消息内容是：" + message.getText());
        MessageProducer producer = session.createProducer(destination);
        Message textMessage = session.createTextMessage("ConsumerSessionAwareMessageListener。。。");
        producer.send(textMessage);
    }

    public Destination getDestination() {
        return destination;
    }

    public void setDestination(Destination destination) {
        this.destination = destination;
    }

}

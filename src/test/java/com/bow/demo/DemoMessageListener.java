package com.bow.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

/**
 * @author vv
 * @since 2017/4/2.
 */
public class DemoMessageListener implements MessageListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(DemoMessageListener.class);

    private String name;

    public DemoMessageListener(String name) {
        this.name = name;
    }

    @Override
    public void onMessage(Message message) {
        try {
            if (message instanceof TextMessage) {
                TextMessage textMessage = (TextMessage) message;
                LOGGER.info(
                        name + " receive " + textMessage.getStringProperty("group") + " : " + textMessage.getText());
            }
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}

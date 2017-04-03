package com.bow.springintegration.service.impl;

import com.bow.springintegration.service.ConsumerService;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.TextMessage;

@Service
public class ConsumerServiceImpl implements ConsumerService {

    @Resource(name = "jmsTemplate")
    private JmsTemplate jmsTemplate;

    public void receiveMessage(Destination destination) throws JMSException {
        TextMessage tm = (TextMessage) jmsTemplate.receive(destination);
        try {
            System.out.println("From " + destination.toString() + " receive:\t" + tm.getText());
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

}

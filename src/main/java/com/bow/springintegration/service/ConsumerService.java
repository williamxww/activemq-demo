package com.bow.springintegration.service;

import javax.jms.Destination;
import javax.jms.JMSException;

public interface ConsumerService {

    void receiveMessage(Destination destination) throws JMSException;

}

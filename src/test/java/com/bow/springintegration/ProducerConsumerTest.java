package com.bow.springintegration;

import javax.jms.Destination;

import com.bow.springintegration.service.ProducerService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class ProducerConsumerTest {

    public static void main(String[] args) throws IOException {
        ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext-jms.xml");
        ProducerService producerService = context.getBean(ProducerService.class);
        Destination queueDestination = context.getBean("queueDestination",Destination.class);

        while (true) {
            System.out.println("pls input message content:\n");
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            String msg = reader.readLine();
            producerService.sendMessage(queueDestination, msg);
        }
    }

}

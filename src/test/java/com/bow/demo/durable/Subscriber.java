/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.bow.demo.durable;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.jms.Topic;

import org.apache.activemq.ActiveMQConnectionFactory;

import com.bow.demo.DemoMessageListener;

/**
 * @author <a href="http://www.christianposta.com/blog">Christian Posta</a>
 */
public class Subscriber {
    private static final String BROKER_URL = "tcp://localhost:61616";

    private static final Boolean NON_TRANSACTED = false;

    Session session;

    private String clientId;

    public Subscriber(String clientId, String url) throws JMSException {
        this.clientId = clientId;
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory("admin", "password", url);
        Connection connection = connectionFactory.createConnection();
        connection.setClientID(clientId);
        connection.start();
        session = connection.createSession(NON_TRANSACTED, Session.AUTO_ACKNOWLEDGE);
    }

    public void durableSubscribe(String topicName, MessageListener listener) throws JMSException {
        Topic topic = session.createTopic(topicName);
        MessageConsumer consumer = session.createDurableSubscriber(topic, clientId);
        consumer.setMessageListener(listener);
    }

    public static void main(String[] args) throws JMSException {
        Subscriber s1 = new Subscriber("vv", BROKER_URL);
        s1.durableSubscribe("test-topic", new DemoMessageListener("s1"));
    }

}
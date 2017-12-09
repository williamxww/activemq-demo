package com.bow.demo.rr;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.broker.BrokerService;

import javax.jms.MessageListener;
import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

/**
 * 参考{@link Client}
 * 
 * @author vv
 * @since 2017/4/2.
 */
public class Server implements MessageListener {
    public static int ackMode = Session.AUTO_ACKNOWLEDGE;

    public static String requestQueueName = "request.messages";

    public static String messageBrokerUrl = "tcp://localhost:61617";

    private Session session;

    private boolean transacted = false;

    private MessageProducer replyProducer;

    public Server() {
        try {
            // 内嵌一个MQ
            BrokerService broker = new BrokerService();
            broker.setPersistent(false);
            broker.setUseJmx(false);
            broker.addConnector(messageBrokerUrl);
            broker.start();
        } catch (Exception e) {
            // Handle the exception appropriately
        }

        this.setupServer();
    }

    private void setupServer() {
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(messageBrokerUrl);
        Connection connection;
        try {
            connection = connectionFactory.createConnection();
            connection.start();
            this.session = connection.createSession(this.transacted, ackMode);

            // replyProducer 用于发送响应
            this.replyProducer = this.session.createProducer(null);
            this.replyProducer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

            // 设置consumer 处理请求
            Destination requestQueue = this.session.createQueue(requestQueueName);
            MessageConsumer consumer = this.session.createConsumer(requestQueue);
            consumer.setMessageListener(this);
        } catch (JMSException e) {
            // Handle the exception appropriately
        }
    }

    /**
     * 处理请求
     * 
     * @param request request
     */
    public void onMessage(Message request) {
        try {
            TextMessage response = this.session.createTextMessage();
            if (request instanceof TextMessage) {
                TextMessage txtMsg = (TextMessage) request;
                System.out.println("receive request " + txtMsg.getText());
                response.setText(" nice! ");
            }
            response.setJMSCorrelationID(request.getJMSCorrelationID());
            // 把响应发到响应队列中
            this.replyProducer.send(request.getJMSReplyTo(), response);
        } catch (JMSException e) {
            // Handle the exception appropriately
        }
    }

    public static void main(String[] args) {
        new Server();
    }
}

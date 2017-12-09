package com.bow.demo.rr;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Random;

/**
 * 请求-响应模式
 * Using the JMS API to implement request-response
 *
 * client在发送请求前先建立一个requestQueue用于存放request供server获取，
 * 然后创建一个临时队列作为responseQueue，服务端有响应时就放在responseQueue里。
 * 注意responseQueue是随请求一起发给服务端的 {@link TextMessage#setJMSReplyTo(Destination)}
 * 参考下面代码
 * 
 * @author vv
 * @since 2017/4/2.
 */
public class Client implements MessageListener {

    private boolean transacted = false;

    private Session session;

    private MessageProducer producer;

    private Destination requestQueue;

    private Destination responseQueue;

    public Client() {
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(Server.messageBrokerUrl);
        Connection connection;
        try {
            connection = connectionFactory.createConnection();
            connection.start();
            session = connection.createSession(transacted, Server.ackMode);
            // 请求队列，请求都发到此队列中
            requestQueue = session.createQueue(Server.requestQueueName);
            this.producer = session.createProducer(requestQueue);
            this.producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

            // 响应队列，server会将响应放到此临时队列中，实际应用中，一个client对应一个队列
            responseQueue = session.createTemporaryQueue();
            MessageConsumer responseConsumer = session.createConsumer(responseQueue);
            // 响应来到时会调用onMessage方法
            responseConsumer.setMessageListener(this);

        } catch (JMSException e) {
            // Handle the exception appropriately
        }
    }

    public void sendRequest(String content) throws JMSException {
        // send request
        TextMessage request = session.createTextMessage();
        request.setText(content);
        // 注意：设置此请求的响应队列
        request.setJMSReplyTo(responseQueue);
        // 设置一个correlationId(requestId)，有响应回来时，知道是对哪条消息做的响应
        request.setJMSCorrelationID(this.createRandomString());
        this.producer.send(request);
    }

    private String createRandomString() {
        Random random = new Random(System.currentTimeMillis());
        long randomLong = random.nextLong();
        return Long.toHexString(randomLong);
    }

    /**
     * 处理响应,这里就是简单的打印
     * 
     * @param response 响应
     */
    public void onMessage(Message response) {
        try {
            if (response instanceof TextMessage) {
                TextMessage textMessage = (TextMessage) response;
                String messageText = textMessage.getText();
                System.out.println("response = " + messageText);
            }
        } catch (JMSException e) {
            // Handle the exception appropriately
        }
    }

    public static void main(String[] args) throws Exception {
        Client client = new Client();
        while (true) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            String request = reader.readLine();
            client.sendRequest(request);

        }
    }
}

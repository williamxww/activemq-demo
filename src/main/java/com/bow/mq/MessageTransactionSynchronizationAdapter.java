package com.bow.mq;

import java.util.List;

import com.bow.entity.QMessage;
import com.bow.service.QMessageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;

public class MessageTransactionSynchronizationAdapter extends TransactionSynchronizationAdapter {

    private static final Logger log = LoggerFactory.getLogger(MessageTransactionSynchronizationAdapter.class);

    private TransactionMessageProducer transactionMessageProducer;

    private QMessageService qMessageService;

    public TransactionMessageProducer getTransactionMessageProducer() {
        return transactionMessageProducer;
    }

    public void setTransactionMessageProducer(TransactionMessageProducer transactionMessageProducer) {
        this.transactionMessageProducer = transactionMessageProducer;
    }

    public QMessageService getqMessageService() {
        return qMessageService;
    }

    public void setqMessageService(QMessageService qMessageService) {
        this.qMessageService = qMessageService;
    }

    @Override
    public int getOrder() {
        return LOWEST_PRECEDENCE;
    }

    @Override
    public void afterCompletion(int status) {
        try {
            if (STATUS_COMMITTED == status) {
                List<String> messageIds = MessageHolder.get();
                sendMessageToBroker(messageIds);
            } else if (STATUS_ROLLED_BACK == status) {
                log.warn("事务提交失败，数据库回滚后，清空缓存中的消息：{}", MessageHolder.get());
            }
        } finally {
            MessageHolder.clear();
        }
    }

    /**
     * 向broker中发送消息
     *
     * @param messageIds
     *            消息id集合
     */
    private void sendMessageToBroker(List<String> messageIds) {
        if (messageIds == null || messageIds.size() == 0) {
            return;
        }
        try {
            for (String messageId : messageIds) {
                QMessage qMessage = qMessageService.getMessage(messageId);
                if (qMessage == null) {
                    continue;
                }
                transactionMessageProducer.sendMessage(qMessage);
            }
        } catch (Exception e) {
            log.error("send message error:{}", e);
        }
    }
}
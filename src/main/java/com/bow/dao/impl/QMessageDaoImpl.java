package com.bow.dao.impl;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bow.entity.QMessage;
import com.bow.dao.QMessageDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

@Repository
public class QMessageDaoImpl implements QMessageDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(QMessageDaoImpl.class);

    private Map<Integer, QMessage> messages = new HashMap();

    /**
     * 根据messageId获取QMessage
     *
     * @param messageId
     * @return
     */
    public QMessage getQMessageByMessageId(String messageId) {
        for (QMessage message : messages.values()) {
            if (messageId.equals(message.getMessageId())) {
                return message;
            }
        }
        LOGGER.warn("find nothing with id: " + messageId);
        return null;
    }

    /**
     * 添加消息
     *
     * @param message 消息
     */
    public int addQMessage(QMessage message) {
        Integer key = message.getId();
        if (key == null) {
            throw new InvalidParameterException("QMessage's id must not be null");
        }
        if (messages.get(key) != null) {
            LOGGER.warn("primary key can not be duplicated");
            return 0;
        } else {
            messages.put(key, message);
            return 1;
        }
    }

    /**
     * 更新消息
     *
     * @param message
     */
    public int updateQMessage(QMessage message) {
        Integer key = message.getId();
        QMessage msg = messages.get(key);
        if (msg == null) {
            return 0;
        }
        messages.put(key, message);
        return 1;
    }

    /**
     * 删除消息
     *
     * @param messageId
     */
    public int deleteQMessage(String messageId) {
        for (QMessage message : messages.values()) {
            if (messageId.equals(message.getMessageId())) {
                messages.remove(messageId);
                return 1;
            }
        }
        LOGGER.warn("find nothing with id: " + messageId);
        return 0;
    }

    /**
     * 获取所有消息
     *
     * @return
     */
    public List<QMessage> getAllQMessage(Long currentTime) {
        return new ArrayList(messages.values());
    }
}

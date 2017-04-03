package com.bow.dao;

import com.bow.entity.QMessage;

import java.util.List;

public interface QMessageDao {

    /**
     * 根据messageId获取QMessage
     *
     * @param messageId
     * @return
     */
    QMessage getQMessageByMessageId(String messageId);

    /**
     * 添加消息
     *
     * @param qMessage 消息
     */
    int addQMessage(QMessage qMessage);

    /**
     * 更新消息
     *
     * @param qMessage
     */
    int updateQMessage(QMessage qMessage);

    /**
     * 删除消息
     *
     * @param messageId
     */
    int deleteQMessage(String messageId);

    /**
     * 获取所有消息
     *
     * @return
     */
    List<QMessage> getAllQMessage(Long currentTime);
}

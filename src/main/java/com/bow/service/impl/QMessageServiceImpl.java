package com.bow.service.impl;

import java.util.List;

import javax.annotation.Resource;

import com.bow.entity.QMessage;
import com.bow.dao.QMessageDao;
import com.bow.service.QMessageService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;


@Service
public class QMessageServiceImpl implements QMessageService {

    @Resource
    private QMessageDao messageDao;

    /**
     * 获取QMessage
     *
     * @param messageId
     * @return
     */
    public QMessage getMessage(String messageId) {
        if (StringUtils.isBlank(messageId)) {
            return null;
        }
        return messageDao.getQMessageByMessageId(messageId);
    }

    /**
     * 添加消息
     *
     * @param qMessage
     */
    public int addQMessage(QMessage qMessage) {
        if (qMessage == null) {
            return 0;
        }
        return messageDao.addQMessage(qMessage);
    }

    /**
     * 更新消息
     *
     * @param qMessage
     */
    public int updateQMessage(QMessage qMessage) {
        if (qMessage == null) {
            return 0;
        }
        return messageDao.updateQMessage(qMessage);
    }

    /**
     * 删除消息
     *
     * @param messageId
     */
    public int deleteQMessage(String messageId) {
        if (StringUtils.isBlank(messageId)) {
            return 0;
        }
        return messageDao.deleteQMessage(messageId);
    }

    /**
     * 获取所有消息
     *
     * @return
     */
    public List<QMessage> selectAllQMessage(Long currentTime) {
        return messageDao.getAllQMessage(currentTime);
    }
}

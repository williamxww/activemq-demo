package com.bow.mq;

import javax.annotation.Resource;

import com.bow.service.QMessageService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.google.common.base.Preconditions;

@Component
public class MessageCallbackImpl implements MessageCallback {

    private static final Logger log = LoggerFactory.getLogger(MessageCallbackImpl.class);

    @Resource
    private QMessageService qMessageService;

    /**
     * 事务消息处理成功
     *
     * @param messageId
     *            消息id
     */
    public void onSuccess(String messageId) {
        Preconditions.checkArgument(StringUtils.isNotBlank(messageId), "messageId must not empty");
        qMessageService.deleteQMessage(messageId);
    }

    /**
     * 事务消息处理失败,进行日志记录
     *
     * @param e
     *            异常
     * @param messageId
     *            消息id
     */
    public void onFail(Exception e, String messageId) {
        log.error("send tx messageId:{},error:{}", messageId, e);
    }
}

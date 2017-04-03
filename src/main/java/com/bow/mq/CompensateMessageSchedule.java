package com.bow.mq;

import java.util.List;

import javax.annotation.Resource;

import com.bow.entity.QMessage;
import com.bow.service.QMessageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


@Component
public class CompensateMessageSchedule {

    private static final Logger log = LoggerFactory.getLogger(CompensateMessageSchedule.class);

    @Resource
    private QMessageService qMessageService;

    @Resource
    private TransactionMessageProducer producer;

    @Scheduled(cron = "0 0/10 * * * ?")
    public void compensateJob() {
        List<QMessage> messages = null;
        try {
            messages = qMessageService.selectAllQMessage(System.currentTimeMillis());
        } catch (Exception e) {
            log.error("query messages error:{}", e);
        }
        //如果没有消息，放弃执行
        if (messages == null || messages.size() == 0) {
            return;
        }
        for (QMessage message : messages) {
            producer.sendMessage(message);
        }
    }
}

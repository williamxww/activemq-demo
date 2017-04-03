package com.bow.dao.impl;

import java.util.Date;


import com.bow.entity.N1Record;
import com.bow.dao.N1RecordDao;
import org.springframework.stereotype.Repository;

@Repository
public class N1RecordDaoImpl implements N1RecordDao {


    /**
     * 根据消息id获取消费记录
     *
     * @param messageId
     *            消息id
     * @return
     */
    public N1Record selectN1Record(String messageId) {
        return null;
    }

    /**
     * 添加消息消费记录
     *
     * @param n1Record
     *            消费记录
     * @return
     */
    public int addN1Record(N1Record n1Record) {
        return 0;
    }

    /**
     * 删除消费记录
     *
     * @param timeStamp
     *            时间戳
     * @return
     */
    public int deleteN1Record(Date timeStamp) {
        return 0;
    }
}

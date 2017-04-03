package com.bow.service.impl;

import java.util.Date;

import javax.annotation.Resource;

import com.bow.entity.N1Record;
import com.bow.dao.N1RecordDao;
import com.bow.service.N1RecordService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service("N1RecordService")
public class N1RecordServiceImpl implements N1RecordService {

    @Resource
    private N1RecordDao n1RecordMapper;

    /**
     * 获取N1Record
     *
     * @param messageId
     * @return
     */
    public N1Record selectN1Record(String messageId) {
        if (StringUtils.isBlank(messageId)) {
            return null;
        }
        return n1RecordMapper.selectN1Record(messageId);
    }

    /**
     * 添加N1Record
     *
     * @param n1Record
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = { Exception.class })
    public int addN1Record(N1Record n1Record) {
        if (n1Record == null) {
            return 0;
        }
        return n1RecordMapper.addN1Record(n1Record);
    }

    /**
     * 删除N1Record
     *
     * @param timeStamp
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = { Exception.class })
    public int deleteN1Record(Date timeStamp) {
        if (timeStamp == null) {
            return 0;
        }
        return n1RecordMapper.deleteN1Record(timeStamp);
    }
}

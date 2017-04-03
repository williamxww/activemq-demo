package com.bow.dao.impl;

import java.util.Map;

import javax.annotation.Resource;

import com.bow.entity.N2Record;
import com.bow.dao.N2RecordDao;
import org.springframework.stereotype.Repository;

@Repository
public class N2RecordDaoImpl implements N2RecordDao {

    /**
     * 根据业务标识查询N2类型的记录
     *
     * @param params 查询参数
     * @return
     */
    public N2Record getN2RecordByMark(Map<String, Object> params) {
        return null;
    }

    /**
     * 根据参数查询N2类型的记录
     *
     * @param params 查询参数
     * @return
     */
    public N2Record getN2Record(Map<String, Object> params) {
        return null;
    }

    /**
     * 添加N2类型的记录
     *
     * @param n2Record N2记录
     * @return
     */
    public int addN2Record(N2Record n2Record) {
        return 0;
    }

    /**
     * 更新N2类型的记录
     *
     * @param n2Record N2类型的记录
     * @return
     */
    public int updateN2Record(N2Record n2Record) {
        return 0;
    }
}

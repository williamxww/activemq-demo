package com.bow.mq;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * ������������oooo������������
 * ��������������������
 * ���������ߩ��������ߩ�
 * ����������������������
 * ����������������������
 * ���������ש������ס���
 * ����������������������
 * �������������ߡ�������
 * ����������������������
 * ����������������������
 * ������������������stay hungry stay foolish
 * ������������������Code is far away from bug with the animal protecting
 * ��������������������������
 * �������������������������ǩ�
 * ����������������������������
 * �������������������ש�����
 * �������������ϩϡ����ϩ�
 * �������������ߩ������ߩ�
 * �����������������թ�����������
 * Module Desc:com.zjp.producer.utils
 * User: zjprevenge
 * Date: 2017/1/23
 * Time: 20:26
 */

public class MessageUtils {

    //�����������
    private static Random random = new Random();

    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMssHHmmssSSS");

    /**
     * ������Ϣid
     *
     * @param date
     * @return
     */
    public static String createMessageId(Date date) {
        String format = dateFormat.format(date);
        String mark = Long.toHexString(random.nextLong());
        return format.concat(mark);
    }
}

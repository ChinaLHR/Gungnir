package io.github.chinalhr.gungnir.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Author : ChinaLHR
 * @Date : Create in 13:41 2018/5/21
 * @Email : 13435500980@163.com
 */
public class TimeStampUtils {

    /**
     * 将时间转换为时间戳
     * @param date 日期时间
     * @param format 格式："yyyy-MM-dd HH:mm:ss"
     * @return
     * @throws ParseException
     */
    public static String dateToStamp(String date,String format) throws ParseException {
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        Date d = simpleDateFormat.parse(date);
        long ts = d.getTime()/ 1000;
        res = String.valueOf(ts);
        return res;
    }

    /**
     * 将时间戳转换为时间
     * @param stamp 时间戳
     * @param format 格式："yyyy-MM-dd HH:mm:ss"
     * @return
     */
    public static String stampToDate(String stamp,String format){
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        long lt = new Long(stamp)*1000;
        Date d = new Date(lt);
        res = simpleDateFormat.format(d);
        return res;
    }

    /**
     * 获取当前时间的UNIX时间戳(秒级别)
     * @return
     */
    public static String getUnixTimeStampWithS(){
        return System.currentTimeMillis()/1000+"";
    }

    /**
     * 获取当前时间的UNIX时间戳(毫秒级别)
     * @return
     */
    public static String getUnixTimeStampWithMS(){
        return System.currentTimeMillis()+"";
    }

}

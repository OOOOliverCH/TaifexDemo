package com.taifexdemo.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

//日期工具類
public class DateUtil {

    private static SimpleDateFormat sdfDate;
    private static Calendar cal;

    static{
        TimeZone zone = TimeZone.getTimeZone("GMT+8");

        sdfDate = new SimpleDateFormat("yyyyMMdd");
        sdfDate.setTimeZone(zone);

        cal = Calendar.getInstance();
        cal.setFirstDayOfWeek(Calendar.MONDAY);
        cal.setTimeZone(zone);
    }

    //取得昨天的日期
    public static String getLastDay(){
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        return sdfDate.format(cal.getTime());
    }

    //取得去年的日期
    public static String getLastYear(){
        Date date = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.YEAR, -1);
        return sdfDate.format(cal.getTime());
    }

    //比較兩個日期
    public static Integer  compareDate(String DATE1, String DATE2, String dateFormat) {
        DateFormat df = new SimpleDateFormat(dateFormat);
        try {
            Date dt1 = df.parse(DATE1);
            Date dt2 = df.parse(DATE2);
            if (dt1.getTime() > dt2.getTime()) {
                return 1;
            } else if (dt1.getTime() < dt2.getTime()) {
                return -1;
            } else {
                return 0;
            }
        } catch (Exception e) {
            return null;
        }
    }
}
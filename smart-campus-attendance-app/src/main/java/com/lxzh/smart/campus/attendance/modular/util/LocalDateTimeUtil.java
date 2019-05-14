package com.lxzh.smart.campus.attendance.modular.util;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.WeekFields;

/**
 * LocalDateTime 时间类工具
 *
 * @author z.y.j
 */
public class LocalDateTimeUtil {

    /**
     * 获取当前指定到的年
     */
    public static Integer yyyy = Integer.valueOf(LocalDateTime.now().toString().substring(0, 4));

    /**
     * 获取当前指定到的月
     */

    public static Integer mm = Integer.valueOf(LocalDateTime.now().toString().substring(5, 7));

    /**
     * 获取当前指定到的天
     */
    public static Integer dd = Integer.valueOf(LocalDateTime.now().toString().substring(8, 10));

    /**
     * String转换为LocalDateTime
     */
    public static LocalDateTime timeSwitch(String localDateTime){
        return LocalDateTime.parse(localDateTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    /**
     * LocalDateTime转换String类型并截取时分(格式：hh:mm)
     */
    public static String timeSwitch(LocalDateTime localDateTime){
        return localDateTime.toString().substring(11,16);
    }

    /**
     * 获取某天凌晨、正午、结束时间（格式为：yyy-MM-ddThh:mm）
     */
    public static LocalDateTime localDateTimeDay(Integer day, LocalTime localTime) {
        //plusDays(day)函数:day选择(0代表当天，-1代表昨天，1代表明天以此类推)；
        //localTime参数(凌晨：LocalTime.MIN,正午：LocalTime.MIDNIGHT，结束：LocalTime.MAX)
        return LocalDateTime.of(LocalDate.now().plusDays(day), localTime);
    }

    /**
     * 根据LocalDateTime时间获取该范围内的时间段(凌晨、正午、结束时间；格式为：yyy-MM-ddThh:mm）
     */
    public static LocalDateTime judgeTimeScope(LocalDateTime localDateTime, LocalTime localTime) {
        Integer introdueYY = localDateTime.getYear();
        Integer introdueMM = Integer.valueOf(localDateTime.toString().substring(5, 7));
        Integer introdueDD = Integer.valueOf(localDateTime.toString().substring(8, 10));
        Integer remainderY = introdueYY - yyyy;
        Integer remainderM = introdueMM - mm;
        Integer remainderD = introdueDD - dd;
        //localTime参数(凌晨：LocalTime.MIN,正午：LocalTime.MIDNIGHT，结束：LocalTime.MAX)
        return LocalDateTime.of(LocalDate.now().plusYears(remainderY).plusMonths(remainderM).plusDays(remainderD), localTime);
    }

    /**
     * 根据传入LocalDateTime时间计算周的方法
     */
    public static Integer sveralWeeks(LocalDateTime localDateTime) {
        return localDateTime.get(WeekFields.of(DayOfWeek.of(1), 1).dayOfWeek());
    }

}

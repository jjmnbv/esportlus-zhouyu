package com.kaihei.esportingplus.common.tools;

import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.Date;

/*/**
 *@Description: jdk8 日期工具类
 *
 *@author  Orochi-Yzh
 *@dateTime  2018/8/21 16:46
 */
public class DateUtil {

    public static final ZoneId chinaZone = ZoneId.systemDefault();
    public static final String SIMPLE_FORMATTER = "yyyyMMddHHmmss";
    public static final String FORMATTER = "yyyy-MM-dd HH:mm:ss";

    /**
     * 获取当天日期(YYYY-MM-DD格式)
     */
    public static LocalDate now() {
        return LocalDate.now();
    }

    /**
     * 按指定格式获取当天日期
     */
    public static String nowDateTime(String formatter) {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern(formatter));
    }

    /**
     * 按指定格式获取当天日期
     */
    public static LocalDateTime dateToLocalDateTime(Date date) {
        return LocalDateTime.ofInstant(date.toInstant(), chinaZone);
    }

    /**
     * 日期时间对象转换为日期对象
     *
     * @param localDateTime 日期时间对象
     * @return 日期对象
     */
    public static LocalDate dateTime2Date(LocalDateTime localDateTime) {
        return localDateTime.toLocalDate();

    }

    /**
     * 日期对象转换为日期对象
     *
     * @param localDate 日期对象
     * @return 日期时间对象
     */
    public static LocalDateTime date2DateTIme(LocalDate localDate) {
        return LocalDateTime.of(localDate, LocalTime.NOON);
    }

    /**
     * 字符串转换为日期
     *
     * @param strDate 字符串日期
     * @return 日期对象 yyyy-mm-dd
     */
    public static LocalDate str2LocalDate(String strDate) {
        return LocalDate.parse(strDate, DateTimeFormatter.ISO_DATE);
    }

    /**
     * 字符串转换为日期
     *
     * @param strDate 字符串日期
     * @return 日期对象 yyyy-mm-dd
     */
    public static LocalDateTime str2LocalDateTime(String strDate, DateTimeFormatter formatter) {
        return LocalDateTime.parse(strDate, formatter);
    }

    /**
     * yyyyMMddHHmmss时间转LocalDateTime
     *
     * @param yyyyMMddHHmmssTime
     * @return
     */
    public static LocalDateTime str2LocalDateTime(String strDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(SIMPLE_FORMATTER);
        return LocalDateTime.parse(strDate, formatter);
    }

    /**
     * LocalDateTime转换为Date
     */
    public static Date localDateTime2Date(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(chinaZone).toInstant());
    }

    /**
     * LocalDateTime转换为Date
     */
    public static Date str2Date(String strDate, String formatter) {
        return localDateTime2Date(str2LocalDateTime(strDate, DateTimeFormatter.ofPattern(formatter)));
    }

    /**
     * yyyyMMddHHmmss时间转Date
     *
     * @param yyyyMMddHHmmssTime
     * @return
     */
    public static Date str2dateWithYMDHMS(String yyyyMMddHHmmssTime) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(SIMPLE_FORMATTER);
        LocalDateTime ldt = LocalDateTime.parse(yyyyMMddHHmmssTime, dtf);
        return localDateTime2Date(ldt);
    }

    /**
     * 日期对象转换为字符串
     *
     * @param localDate 日期对象
     * @return 日期字符串 yyyy-mm-dd
     */
    public static String date2Str(LocalDate localDate) {
        return localDate.format(DateTimeFormatter.ISO_DATE);
    }

    /**
     * 日期时间对象转换为字符串
     *
     * @param localDateTime     日期时间对象
     * @param dateTimeFormatter 格式化字符串
     * @return 日期字符串
     */
    public static String dateTime2Str(LocalDateTime localDateTime, String dateTimeFormatter) {
        return localDateTime.format(DateTimeFormatter.ofPattern(dateTimeFormatter));
    }

    /**
     * 日期时间转字符串函数 返回ISO标准的日期字符串
     *
     * @param localDateTime 日期时间对象
     * @return 日期字符串
     */
    public static String dateTime2Str(LocalDateTime localDateTime) {
        return localDateTime.format(DateTimeFormatter.ISO_DATE_TIME);
    }

    /**
     * 获取东八时间戳,秒级
     */
    public static long getTimeStrampMiniseconds() {
        //获取秒数
        return LocalDateTime.now().toEpochSecond(ZoneOffset.of("+8"));
    }

    /**
     * 获取东八时间戳,毫秒级
     */
    public static long getTimeStrampSeconds() {
        //获取秒数
        return LocalDateTime.now().toInstant(ZoneOffset.of("+8")).toEpochMilli();
    }

    /**
     * date 转string： yyyy-MM-dd HH:mm:ss
     */
    public static String fromDate2Str(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return simpleDateFormat.format(date);
    }

    /**
     * 计算两个时间之间相差的秒数
     *
     * @param date1 起始时间
     * @param date2 结束时间
     */
    public static long secondsBetween(LocalDateTime date1, LocalDateTime date2) {
        Duration duration = Duration.between(date1, date2);
        return duration.getSeconds();
    }

    /**
     * 计算两个日期之间相差的天数
     *
     * @param date1 起始日期
     * @param date2 结束日期
     */
    public static int daysBetween(LocalDate date1, LocalDate date2) {
        Period period = Period.between(date1, date2);
        return period.getDays();
    }

    /**
     * 计算两个日期之间相差的月数
     *
     * @param date1 起始日期
     * @param date2 结束日期
     */
    public static int monthsBetween(LocalDate date1, LocalDate date2) {
        Period period = Period.between(date1, date2);
        return period.getMonths();
    }

    /**
     * 计算两个日期之间相差的年数
     *
     * @param date1 起始日期
     * @param date2 结束日期
     */
    public static int yearsBetween(LocalDate date1, LocalDate date2) {
        Period period = Period.between(date1, date2);
        return period.getYears();
    }

    /**
     * 计算两个日期之间相差的天数
     *
     * @param date1 起始日期
     * @param date2 结束日期
     */
    public static int daysBetween(Date date1, Date date2) {
        Instant instantDate1 = date1.toInstant();
        Instant instantDate2 = date2.toInstant();
        LocalDate localDate1 = instantDate1.atZone(chinaZone).toLocalDate();
        LocalDate localDate2 = instantDate2.atZone(chinaZone).toLocalDate();
        instantDate1.atZone(chinaZone);
        Period period = Period.between(localDate1, localDate2);
        return period.getDays();
    }

    /**
     * 计算两个日期之间相差的月数
     *
     * @param date1 起始日期
     * @param date2 结束日期
     */
    public static int monthsBetween(Date date1, Date date2) {
        Instant instantDate1 = date1.toInstant();
        Instant instantDate2 = date2.toInstant();
        LocalDate localDate1 = instantDate1.atZone(chinaZone).toLocalDate();
        LocalDate localDate2 = instantDate2.atZone(chinaZone).toLocalDate();
        instantDate1.atZone(chinaZone);
        Period period = Period.between(localDate1, localDate2);
        return period.getMonths();
    }

    /**
     * 计算两个日期之间相差的年数
     *
     * @param date1 起始日期
     * @param date2 结束日期
     */
    public static int yearsBetween(Date date1, Date date2) {
        Instant instantDate1 = date1.toInstant();
        Instant instantDate2 = date2.toInstant();
        LocalDate localDate1 = instantDate1.atZone(chinaZone).toLocalDate();
        LocalDate localDate2 = instantDate2.atZone(chinaZone).toLocalDate();
        instantDate1.atZone(chinaZone);
        Period period = Period.between(localDate1, localDate2);
        return period.getYears();
    }

    /**
     * 获取指定日期对象当前月的起始日
     *
     * @param localDate 指定日期
     */
    public static int getFirstDayInMonth(LocalDate localDate) {
        LocalDate result = localDate.with(TemporalAdjusters.firstDayOfMonth());
        return result.getDayOfMonth();

    }

    /**
     * 获取指定日期对象的当前月的结束日
     *
     * @param localDate 指定日期
     */
    public static int getLastDayInMonth(LocalDate localDate) {
        LocalDate result = localDate.with(TemporalAdjusters.lastDayOfMonth());
        return result.getDayOfMonth();
    }

    /**
     * 获取指定日期对象本月的某周某天的日期
     *
     * @param localDate  日期对象
     * @param weekNumber 周
     * @param dayNumber  日
     */
    public static LocalDate getLocalDateBydayAndWeek(LocalDate localDate, int weekNumber, int dayNumber) {
        return localDate.with(TemporalAdjusters.dayOfWeekInMonth(weekNumber, DayOfWeek.of(dayNumber)));
    }

    public static String getHour(LocalDateTime localDate) {
        return dateTime2Str(localDate, SIMPLE_FORMATTER).substring(8, 10);
    }

    public static String getDay(LocalDateTime localDate) {
        return dateTime2Str(localDate, SIMPLE_FORMATTER).substring(6, 8);
    }

    public static void main(String[] args) throws InterruptedException {
        System.out.println(DateUtil
                .str2Date("20180814140659", DateUtil.SIMPLE_FORMATTER));

        System.out.println(DateUtil.nowDateTime(DateUtil.SIMPLE_FORMATTER));

        LocalDateTime now = LocalDateTime.now();
        Thread.sleep(41000);
        LocalDateTime now2 = LocalDateTime.now();
        System.out.println(DateUtil.secondsBetween(now,now2));
    }
}
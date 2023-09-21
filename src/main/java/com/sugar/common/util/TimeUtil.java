package com.sugar.common.util;

import lombok.extern.slf4j.Slf4j;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoField;
import java.util.Date;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 时间工具类
 *
 * @author astupidcoder
 */
@Slf4j
public class TimeUtil {

    /**
     * 1分钟的秒时间
     */
    public static final long MINUTE_SECONDS = 60L;

    /**
     * 1小时的分钟时间
     */
    public static final long HOUR_MINUTES = 60L;

    /**
     * 一分钟的毫秒时长
     */
    public static final long MINUTE_MILLIS = MINUTE_SECONDS * 1000;

    /**
     * 一小时的毫秒时长
     */
    public static final long HOUR_MILLIS = 60L * MINUTE_MILLIS;
    /**
     * 一天的毫秒时长
     */
    public static final long DAY_MILLIS = 24L * HOUR_MILLIS;

    /**
     * 一天的分鐘时长
     */
    public static final int DAY_MINUTES = (int) (24 * MINUTE_SECONDS);

    /**
     * 一天的秒时长
     */
    public static final long DAY_SECONDS = DAY_MINUTES * MINUTE_SECONDS;

    /**
     * 显示时间的格式
     */
    public final static String DATE_PATTERN = "yyyy-MM-dd";
    /**
     * 1秒的时长
     */
    public static final long ONE_MILLS = 1000L;

    /**
     * 2015-02-23 12:12:12格式
     */
    public static final String DEFAULT_FORMAT = "yyyy-MM-dd HH:mm:ss";
    /**
     * 显示时间的格式
     */
    public final static String DATETIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
    /**
     * 时间格式 2019-03-09-00-00-00
     */
    public static final String TIME_FORMAT_1 = "yyyy-MM-dd-HH-mm-ss";

    /**
     * 时间格式 2019-03-09-00-00-00
     */
    public static final String TIME_FORMAT_2 = "yyyyMMddHHmmss";

    /**
     * 验证日期字符串是否是YYYY-MM-dd格式或者YYYYMMdd
     */
    public static boolean checkDateFormat(String str) {
        boolean flag = false;
        String regex = "^((\\d{2}(([02468][048])|([13579][26]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][1235679])|([13579][01345789]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|(1[0-9])|(2[0-8]))))))(\\s(((0?[0-9])|([1-2][0-3]))\\:([0-5]?[0-9])((\\s)|(\\:([0-5]?[0-9])))))?$";
        Pattern pattern1 = Pattern.compile(regex);
        Matcher isNo = pattern1.matcher(str);
        if (isNo.matches()) {
            flag = true;
        }
        return flag;
    }

    public static int getNowOfSeconds() {
        return (int) (System.currentTimeMillis() / 1000);
    }

    /**
     * 获取每天0点开始，每过2个小时的时间戳
     *
     * @param point 间隔小时，例如:间隔2小时，就传2，单位：小时
     */
    public static long getNowEvenTime(int point) {
        if (point <= 0) {
            return 0;
        }
        int hourTest = getNowHour();
        int mo = hourTest / point;
        int hour = (mo + 1) * point;
        int dayAdd = 0;
        if (hour >= 24) { //跨天
            hour = 0;
            //TODO  可能增加几天
            dayAdd = 1;
        }

        LocalDateTime localDateTime = LocalDateTime.of(LocalDateTime.now().getYear(), LocalDateTime.now().getMonth(), LocalDateTime.now().getDayOfMonth(), hour, 0, 0);
        if (dayAdd > 0) {
            localDateTime = localDateTime.plusDays(dayAdd);
        }
        ZoneId zone = ZoneId.systemDefault();
        Instant instant = localDateTime.atZone(zone).toInstant();
        return instant.toEpochMilli();
    }

    public static long getNowOfMills() {
        return System.currentTimeMillis();
    }

    /**
     * 获取距离当天零点固定时长的时间戳
     *
     * @param hour 时 例如：获取当天早上9：00传9，晚上21：00传21
     */
    public static long getNowHourTimeMilli(int hour) {
        if (hour <= 0) {
            return 0;
        }
        LocalDateTime localDateTime = LocalDateTime.of(LocalDateTime.now().getYear(), LocalDateTime.now().getMonth(), LocalDateTime.now().getDayOfMonth(), hour, 0, 0);
        ZoneId zone = ZoneId.systemDefault();
        Instant instant = localDateTime.atZone(zone).toInstant();
        return instant.toEpochMilli();
    }

    /**
     * 获得0点的时间戳
     *
     * @return
     */
    public static long getNowZeroTimeMilli() {
        long current = System.currentTimeMillis();
        return current - (current + TimeZone.getDefault().getRawOffset()) % (1000 * 3600 * 24);
    }

    /**
     * 获取当前时间的小时数
     *
     * @return
     */
    public static int getNowHour() {
        return LocalDateTime.now().getHour();
    }

    /**
     * 获取当前是周几
     *
     * @return dayOfWeek
     */
    public static int getDayOfWeek() {
        return LocalDateTime.now().getDayOfWeek().getValue();
    }

    /**
     * 判断两个时间是否是同一天
     */
    public static boolean isSameDay(long sourceTime, long targetTime) {
        Instant instant1 = Instant.ofEpochMilli(sourceTime);
        LocalDateTime localDateTime1 = LocalDateTime.ofInstant(instant1, ZoneId.systemDefault());
        long day1 = localDateTime1.getLong(ChronoField.EPOCH_DAY);

        Instant instant2 = Instant.ofEpochMilli(targetTime);
        LocalDateTime localDateTime2 = LocalDateTime.ofInstant(instant2, ZoneId.systemDefault());
        long day2 = localDateTime2.getLong(ChronoField.EPOCH_DAY);

        return day1 == day2;

    }

    /**
     * 获取time日零点秒时间
     *
     * @param time 时间（毫秒）
     */
    public static int dayZeroSecondsFromTime(long time) {
        return (int) (dayZeroMillsFromTime(time * 1000L) / 1000);
    }

    /**
     * 获取time日零点毫秒时间
     *
     * @param time 时间 （毫秒）
     */
    public static long dayZeroMillsFromTime(long time) {
        Instant instant = Instant.ofEpochMilli(time);
        LocalDateTime dt = LocalDateTime.ofInstant(instant, ZoneId.systemDefault())
                .withHour(0)
                .withMinute(0)
                .withSecond(0)
                .withNano(0);
        return dt.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }


    public static String nowFormat() {
        return TimeUtil.timeFormat(getNowOfMills());
    }

    public static String timeFormat(long time) {
        return TimeUtil.timeFormat(time, DEFAULT_FORMAT);
    }

    /**
     * 格式化时间
     *
     * @param time
     * @param format
     * @return
     */
    public static String timeFormat(long time, String format) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(format);
        LocalDateTime ldt = LocalDateTime.ofInstant(Instant.ofEpochMilli(time), ZoneId.systemDefault());
        return ldt.format(dtf);
    }

    public static String timeFormat(LocalDateTime time, String format) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(format);
        return time.format(dtf);
    }

    public static Date timeFormat(String dateStr, String format) {
        DateFormat df = new SimpleDateFormat(format);
        try {
            return df.parse(dateStr);
        } catch (Exception e) {
            log.error("日期转换错误:{}", dateStr);
            return null;
        }
    }

    public static Long timeParseMills(String dateStr, String format) {

        try {
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern(format);
            LocalDateTime dateTime = LocalDateTime.parse(dateStr, dtf);

            return dateTime.toInstant(ZoneOffset.of("+8")).toEpochMilli();

        } catch (DateTimeParseException | NullPointerException e) {
            return null;
        }
    }

    public static Integer timeParseSeconds(String dateStr, String format) {
        Long mills = timeParseMills(dateStr, format);
        if (mills == null) {
            return null;
        }
        return (int) (mills / 1000);
    }

    /**
     * 获取今天已经过去的分钟数
     *
     * @return
     */
    public static int getTodayOfMinute() {
        int nowOfMinutes = getNowOfMinutes();
        int zeroMinuteFromNow = dayZeroMinuteFromNow();
        return nowOfMinutes - zeroMinuteFromNow;
    }

    /**
     * 获取今天已经过去的秒数
     *
     * @return
     */
    public static int getTodayOfSecond() {
        int nowOfSeconds = getNowOfSeconds();
        int zeroSecondFromNow = dayZeroSecondsFromNow();
        return nowOfSeconds - zeroSecondFromNow;
    }

    public static int getNowOfMinutes() {
        return (int) (System.currentTimeMillis() / 1000 / 60);
    }

    /**
     * 获取今天零点的分钟数
     */
    public static int dayZeroMinuteFromNow() {
        LocalDateTime localDateTime = LocalDate.now().atStartOfDay();
        return (int) (localDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli() / 60000);
    }

    /**
     * 获取今天零点的秒数
     */
    public static int dayZeroSecondsFromNow() {
        return (int) (dayZeroMillsFromNow() / ONE_MILLS);
    }

    public static long dayZeroMillsFromNow() {
        LocalDateTime localDateTime = LocalDate.now().atStartOfDay();
        return localDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

    /**
     * 判断今天是否为某年的同一周
     *
     * @return boolean
     */
    public static boolean isSameWeek(long oldTime) {
        LocalDateTime nowTime = LocalDateTime.now();
        Instant instant = Instant.ofEpochMilli(oldTime);
        LocalDateTime dateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());

        int nowIndex = nowTime.getDayOfWeek().getValue();
        int oldIndex = dateTime.getDayOfWeek().getValue();
        if (nowTime.getYear() != dateTime.getYear() && oldIndex == nowIndex) {
            return false;
        }

        nowTime = nowTime.plusDays(7 - nowIndex).withHour(0).withMinute(0).withSecond(0).withNano(0);
        dateTime = dateTime.plusDays(7 - oldIndex).withHour(0).withMinute(0).withSecond(0).withNano(0);

        return dateTime.compareTo(nowTime) == 0;
    }

    /**
     * 判断指定的时间是否是今天
     *
     * @param time 毫秒数
     * @return
     */
    public static boolean isToday(long time) {
        Instant instant = Instant.ofEpochMilli(time);
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
        return localDateTime.toLocalDate().isEqual(LocalDate.now());
    }

    /**
     * 获取距离time的自然天数
     *
     * @param time
     * @return
     */
    public static int getNaturalDayFromTime(long time) {
        Instant instant = Instant.ofEpochMilli(time);
        LocalDateTime ldt = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
        long day = ldt.getLong(ChronoField.EPOCH_DAY);
        long nowDay = LocalDate.now().getLong(ChronoField.EPOCH_DAY);
        return (int) (nowDay - day);
    }

    /**
     * 通过时间秒毫秒数判断两个时间的间隔天数
     *
     * @param time1
     * @param time2
     * @return
     */
    public static int betweenDay(long time1, long time2) {
        return (int) (Math.abs(time1 - time2) / DAY_MILLIS);
    }

    /**
     * 获取指定时间的自然天数
     *
     * @param time
     * @return
     */
    public static long getDay(long time) {
        Instant instant = Instant.ofEpochMilli(time);
        LocalDateTime ldt = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());

        return ldt.getLong(ChronoField.EPOCH_DAY);
    }

    public static LocalDateTime getDateTimeOfMillis(long millis) {
        Instant instant = Instant.ofEpochMilli(millis);
        ZoneId zone = ZoneId.systemDefault();
        return LocalDateTime.ofInstant(instant, zone);
    }

    /**
     * 获取指定日期的时间戳
     *
     * @param month 从1开始
     */
    public static long getTimeInMillis(int year, int month, int day, int hour, int minute, int second,
                                       int milliSecond) {
        LocalDateTime ldt = LocalDateTime.of(year, month, day, hour, minute, second, milliSecond * 1000_000);
        return ldt.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

    public static boolean isTodayByHour(Date date, int hour) {
        // 获取当前日期的指定小时时间
        long nowHourTime = getTimeByHour(hour);
        long nowTime = System.currentTimeMillis();
        // date的时间戳
        long lastTime = date.getTime();
        if (lastTime < nowHourTime && nowTime >= nowHourTime) {
            return false;
        }
        // 另外一种情况 0< 现在时间<5
        long cur0time = nowHourTime - hour * TimeConstant.ONE_HOUR_MILLISECOND; // 当天0点时间
        if (nowTime > cur0time && nowTime < nowHourTime) {
            // 前一天的这个时间点
            long firstHourTime = nowHourTime - TimeConstant.ONE_DAY_MILLISECOND;
            if (lastTime < firstHourTime) {
                return false;
            }
        }
        return true;
    }
        /**
         * <p>getTimeByHour.</p>
         *
         * @param hour a int.
         * @return 时间戳
         * @Description: 根据小时取得当天指定整点时的时间戳
         * @author wm
         * @date 2015-11-20
         */
        public static long getTimeByHour(int hour) {
            Timestamp now = nowTimestamp();
            String timeStr = getDate(now) + " " + hour + ":00:00" + ".000";
            Timestamp pointTime = str2Date(timeStr);
            return pointTime.getTime();
        }

    /**
     * Return a Timestamp for right now 可以用于sql
     *
     * @return a {@link java.sql.Timestamp} object.
     */
    public static Timestamp nowTimestamp() {
        return new Timestamp(System.currentTimeMillis());
    }

    /**
     * Timestamp to Str, 只要日期部分yyyy-MM-dd
     *
     * @param dttm date
     * @return a {@link java.lang.String} object.
     */
    public static String getDate(Timestamp dttm) {
        if (dttm == null)  return null;
        SimpleDateFormat lFormatTimestamp = new SimpleDateFormat(DATE_PATTERN);
        return lFormatTimestamp.format(dttm);
    }

    /**
     * <p>str2Date.</p>
     *
     * @param asDate a {@link java.lang.String} object.
     * @return a {@link java.sql.Timestamp} object.
     */
    public static Timestamp str2Date(String asDate) {
        return str2Date(asDate, DATETIME_PATTERN);
    }
    /**
     * string to timestamp, with given pattern
     *
     * @param asDate    a {@link java.lang.String} object.
     * @param asPattern a {@link java.lang.String} object.
     * @return a {@link java.sql.Timestamp} object.
     */
    public static Timestamp str2Date(String asDate, String asPattern) {
        Timestamp lStamp = null;

        if (isEmpty(asDate)) {
            return null;
        }

        if (isEmpty(asPattern)) {
            try {
                lStamp = Timestamp.valueOf(asDate);
            } catch (Exception e) {
                return null;
            }
        } else {
            try {
                SimpleDateFormat lFormat = new SimpleDateFormat(asPattern);
                lStamp = new Timestamp(lFormat.parse(asDate).getTime());
            } catch (Exception e) {
                return null;
            }
        }

        return lStamp;
    }

    /**
     * <p>isEmpty.</p>
     *
     * @param s a {@link java.lang.String} object.
     * @return a boolean.
     */
    public static boolean isEmpty(String s) {
        return ((s == null) || (s.trim().length() == 0));
    }

}

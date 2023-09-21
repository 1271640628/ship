package com.sugar.common.util;


/**
 * 时间单位静态定义
 */
public interface TimeConstant {

    /**
     * 如果超过这个时间，判断不在线
     */
    long LEAVEGAME_TIME = 60 * 1000;

    /**
     * 时间戳 秒 单位的最大值
     */
    long TIMESTAMP_SECOND_MAX = 9_999_999_999L;
    /**
     * 时间戳 秒 单位的最小值
     */
    long TIMESTAMP_SECOND_MIN = 1_000_000_000L;
    /**
     * 时间戳 毫秒的最大值
     */
    long TIMESTAMP_MILLISECOND_MAX = 9_999_999_999_999L;
    /**
     * 时间戳 毫秒单位的最小值
     */
    long TIMESTAMP_MILLISECOND_MIN = 1_000_000_000_000L;
    /**
     * 一秒钟的毫秒数
     */
    long ONE_SECOND_MILLISECOND = 1000;

    /**
     * 一分钟的毫秒数
     */
    long ONE_MINUTE_MILLISECOND = 60 * ONE_SECOND_MILLISECOND;

    /**
     * 一小时的毫秒数
     */
    long ONE_HOUR_MILLISECOND = 60 * ONE_MINUTE_MILLISECOND;

    /**
     * 一天的毫秒数
     */
    long ONE_DAY_MILLISECOND = 24 * ONE_HOUR_MILLISECOND;


    /**
     * 一周毫秒数
     */
    long ONE_WEEK_MILLISECOND = 7 * ONE_DAY_MILLISECOND;
    /**
     * 一个月的毫秒数 按30天计算
     */
    long ONE_MONTH_MILLISECOND = 30 * ONE_DAY_MILLISECOND;

    /**
     * 一分钟的秒数
     */
    long ONE_MINUTE_SECOND = ONE_MINUTE_MILLISECOND / ONE_SECOND_MILLISECOND;

    /**
     * 1小时的秒数
     */
    long ONE_HOUR_SECOND = ONE_HOUR_MILLISECOND / ONE_SECOND_MILLISECOND;

    /**
     * 1天的秒数
     */
    long ONE_DAY_SECOND = ONE_DAY_MILLISECOND / ONE_SECOND_MILLISECOND;

    /**
     * 一个月的秒数
     */
    long ONE_MONTH_SECOND = ONE_MONTH_MILLISECOND / ONE_SECOND_MILLISECOND;

}

package com.sugar.common.util;


import static com.sugar.common.util.IdUtils.DataId.*;
import static com.sugar.common.util.IdUtils.WorkerId.SYSTEM;

/**
 * 雪花ID生成工具 范围为0-30
 * 分区组合：
 * 国家分区 - 地区分区
 * 平台分区 - 服务器分区
 * 业务分区 - 类型分区
 *
 * @author astupidcoder
 */
public class IdUtils {

    /**
     * 大分区 0-30
     * 可为 国家分区
     */
    interface WorkerId {

        int SYSTEM = 0;
    }

    /**
     * 小分区 0-30
     * 可为地区分区
     */
    interface DataId {
        // 用户名
        int UID = 0;
        // 充值订单
        int ORDER = 1;
        // 金额订单
        int MONEY = 2;
        // 下注表-下注为了优化获取当天0点之前的订单，已改为预设ID，勿删
        int PLAY = 3;
        // 建议
        int SUGGEST = 4;
    }

    /**
     * 用户名生成工具
     */
    private static final IdUtils SYS_UID_UTIL = new IdUtils(SYSTEM, UID);
    private static final IdUtils SYS_ORDER_UTIL = new IdUtils(SYSTEM, ORDER);
    private static final IdUtils SYS_MONEY_UTIL = new IdUtils(SYSTEM, MONEY);
    private static final IdUtils SYS_PLAY_UTIL = new IdUtils(SYSTEM, PLAY);
    private static final IdUtils SYS_SUGGEST_UTIL = new IdUtils(SYSTEM, SUGGEST);

    public static IdUtils getSysUidUtil() {
        return SYS_UID_UTIL;
    }

    public static IdUtils getSysOrderUtil() {
        return SYS_ORDER_UTIL;
    }

    public static IdUtils getSysMoneyUtil() {
        return SYS_MONEY_UTIL;
    }

    public static IdUtils getSysPlayUtil() {
        return SYS_PLAY_UTIL;
    }
    public static IdUtils getSysSuggestUtil() {
        return SYS_SUGGEST_UTIL;
    }

    /**
     * 开始时间截 (2015-01-01)
     */
    private final long twepoch = 1420041600000L;

    /**
     * 机器id所占的位数
     */
    private final long workerIdBits = 5L;

    /**
     * 数据标识id所占的位数
     */
    private final long datacenterIdBits = 5L;

    /**
     * 支持的最大机器id，结果是31 (这个移位算法可以很快的计算出几位二进制数所能表示的最大十进制数)
     */
    private final long maxWorkerId = -1L ^ (-1L << workerIdBits);

    /**
     * 支持的最大数据标识id，结果是31
     */
    private final long maxDatacenterId = -1L ^ (-1L << datacenterIdBits);

    /**
     * 序列在id中占的位数
     */
    private final long sequenceBits = 12L;

    /**
     * 机器ID向左移12位
     */
    private final long workerIdShift = sequenceBits;

    /**
     * 数据标识id向左移17位(12+5)
     */
    private final long datacenterIdShift = sequenceBits + workerIdBits;

    /**
     * 时间截向左移22位(5+5+12)
     */
    private final long timestampLeftShift = sequenceBits + workerIdBits + datacenterIdBits;

    /**
     * 生成序列的掩码，这里为4095 (0b111111111111=0xfff=4095)
     */
    private final long sequenceMask = -1L ^ (-1L << sequenceBits);

    /**
     * 工作机器ID(0~31)
     */
    private long workerId;

    /**
     * 数据中心ID(0~31)
     */
    private long datacenterId;

    /**
     * 毫秒内序列(0~4095)
     */
    private long sequence = 0L;

    /**
     * 上次生成ID的时间截
     */
    private long lastTimestamp = -1L;


    /**
     * 构造函数
     *
     * @param workerId     工作ID (0~31)
     * @param datacenterId 数据中心ID (0~31)
     */
    public IdUtils(long workerId, long datacenterId) {
        if (workerId > maxWorkerId || workerId < 0) {
            throw new IllegalArgumentException(String.format("worker Id can't be greater than %d or less than 0", maxWorkerId));
        }
        if (datacenterId > maxDatacenterId || datacenterId < 0) {
            throw new IllegalArgumentException(String.format("datacenter Id can't be greater than %d or less than 0", maxDatacenterId));
        }
        this.workerId = workerId;
        this.datacenterId = datacenterId;
    }

    // ==============================Methods==========================================

    /**
     * 获得下一个ID (该方法是线程安全的)
     */
    public synchronized long getId() {
        long timestamp = timeGen();

        //如果当前时间小于上一次ID生成的时间戳，说明系统时钟回退过这个时候应当抛出异常
        if (timestamp < lastTimestamp) {
            throw new RuntimeException(
                    String.format("Clock moved backwards.  Refusing to generate id for %d milliseconds", lastTimestamp - timestamp));
        }

        //如果是同一时间生成的，则进行毫秒内序列
        if (lastTimestamp == timestamp) {
            sequence = (sequence + 1) & sequenceMask;
            //毫秒内序列溢出
            if (sequence == 0) {
                //阻塞到下一个毫秒,获得新的时间戳
                timestamp = tilNextMillis(lastTimestamp);
            }
        }
        //时间戳改变，毫秒内序列重置
        else {
            sequence = 0L;
        }

        //上次生成ID的时间截
        lastTimestamp = timestamp;

        //移位并通过或运算拼到一起组成64位的ID
        return ((timestamp - twepoch) << timestampLeftShift)
                | (datacenterId << datacenterIdShift)
                | (workerId << workerIdShift)
                | sequence;
    }

    /**
     * 阻塞到下一个毫秒，直到获得新的时间戳
     *
     * @param lastTimestamp 上次生成ID的时间截
     * @return 当前时间戳
     */
    protected long tilNextMillis(long lastTimestamp) {
        long timestamp = timeGen();
        while (timestamp <= lastTimestamp) {
            timestamp = timeGen();
        }
        return timestamp;
    }

    /**
     * 返回以毫秒为单位的当前时间
     *
     * @return 当前时间(毫秒)
     */
    protected long timeGen() {
        return TimeUtil.getNowOfMills();
    }

    //==============================Test=============================================

    /**
     * 测试
     */

}
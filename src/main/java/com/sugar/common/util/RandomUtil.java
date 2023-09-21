package com.sugar.common.util;


import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;


/**
 * 随机工具类
 *
 * @author astupidcoder
 */
@Slf4j
public class RandomUtil {

    private static final IllegalArgumentException EMPTY_COLLECTION_EXCEPTION = new IllegalArgumentException("元素集不能为空！");
    private static final IllegalArgumentException AREA_PARAM_EXCEPTION = new IllegalArgumentException("传入的范围不合法!最大值必须大于最小值！");

    public static int min(int a, int b) {
        return Math.min(a, b);
    }

    /**
     * 随机产生 min-max间 一个整数值
     */
    public static int random(int min, int max) {
        if (min > max) {
            throw AREA_PARAM_EXCEPTION;
        }
        // [min,Max]
        return ThreadLocalRandom.current().nextInt(max - min + 1) + min;
    }

    /**
     * 随机产生 min-max间 一个长整型
     */
    public static long random(long min, long max) {
        if (min > max) {
            throw AREA_PARAM_EXCEPTION;
        }
        // [min,Max]
        return ThreadLocalRandom.current().nextLong(max - min + 1) + min;
    }

    /**
     * 随机产生 min-max间 一个小数值
     */
    public static double random(double min, double max) {
        if (min >= max) {
            throw AREA_PARAM_EXCEPTION;
        }
        // [min,Max)
        return ThreadLocalRandom.current().nextDouble(min, max);
    }


    /**
     * 随机一个double类型的数
     */
    public static Double randomDouble(Double min, Double max) {
        if (min > max) {
            throw AREA_PARAM_EXCEPTION;
        }
        if (min.equals(max)) {
            return min;
        }
        DecimalFormat df = new DecimalFormat("#.0");
        Double num = ThreadLocalRandom.current().nextDouble(max - min) + min;
        return Double.valueOf(df.format(num));
    }

    /**
     * 根据几率计算是否生成，成功几率是sucRange/maxRange
     *
     * @param maxRange 最大范围，随机范围是[1,maxRange]
     * @param sucRange 成功范围，成功范围是[1,sucRange]
     * @return 成功true失败false
     */
    public static boolean isGenerate(int maxRange, int sucRange) {
        return maxRange == sucRange || sucRange > 0 && random(1, maxRange) <= sucRange;
    }

    /**
     * 根据几率计算是否生成及次数，成功几率是sucRange/maxRange
     * 例：Fx(50/100) = isGenerate(100 : 50);
     * Fx(250/100) = 2 + isGenerate(100 : 50);
     *
     * @param maxRange 最大范围，随机范围是[1,maxRange]
     * @param sucRange 成功范围，成功范围是[1,sucRange]
     * @return 成功则大于0
     */
    public static int timesGenerate(int maxRange, int sucRange) {
        return (sucRange / maxRange) + (isGenerate(maxRange, (sucRange % maxRange)) ? 1 : 0);
    }

    /**
     * 从指定的的元素集中随机一个元素
     *
     * @param collection 元素集
     */
    public static <T> T randomElement(Collection<T> collection) {
        if (collection == null || collection.isEmpty()) {
            throw EMPTY_COLLECTION_EXCEPTION;
        }
        int index = random(0, collection.size() - 1);
        Iterator<T> it = collection.iterator();
        for (int i = 0; i <= index && it.hasNext(); i++) {
            T t = it.next();
            if (i == index) {
                return t;
            }
        }
        return null;
    }

    /**
     * 从指定集合中随机count个不重复的元素
     *
     * @param collection 集合
     * @param count      随机数量
     */
    public static <T> List<T> randomElements(Collection<T> collection, int count) {
        if (collection == null || collection.isEmpty()) {
            throw EMPTY_COLLECTION_EXCEPTION;
        }

        List<T> ret = Lists.newArrayList();
        if (collection.size() <= count) {
            ret.addAll(collection);
        } else {
            List<Integer> indexs = Lists.newArrayListWithCapacity(count);
            while (indexs.size() < count) {
                int index = random(0, collection.size() - 1);
                if (!indexs.contains(index)) {
                    indexs.add(index);
                }
            }

            Iterator<T> it = collection.iterator();
            for (int i = 0; i < collection.size() && it.hasNext(); i++) {
                T t = it.next();
                if (indexs.contains(i)) {
                    ret.add(t);
                }
            }
        }
        return ret;
    }

    /**
     * 从指定的元素数组中随机出一个元素
     *
     * @param array 元素数组
     */
    public static <T> T randomElement(T[] array) {
        if (array == null || array.length == 0) {
            throw EMPTY_COLLECTION_EXCEPTION;
        }
        return randomElement(Arrays.asList(array));
    }

    /**
     * 根据每个几率返回随机的一个索引
     *
     * @param map k - key对象 v - prob
     * @return null失败or随机的索引
     */
    public static <T> T randomKeyByProb(Map<T, Integer> map) {
        if (map == null) {
            return null;
        }
        int i = randomIndexByProb(Lists.newArrayList(map.values()));
        if (i == -1) {
            return null;
        }
        return Lists.newArrayList(map.keySet()).get(i);
    }

    /**
     * 根据每个几率返回随机的多个个索引 可返回重复的
     *
     * @param num 返回适量
     * @param map k - key对象 v - prob
     * @return list.size==0失败 or随机的索引列表
     */
    public static <T> List<T> randomKeysByProbCanRepeat(Map<T, Integer> map, int num) {
        ArrayList<T> list = Lists.newArrayList();
        if (map == null) {
            return list;
        }

        for (int i = 0; i < num; i++) {
            int index = randomIndexByProb(Lists.newArrayList(map.values()));
            if (index == -1) {
                return list;
            }
            list.add(Lists.newArrayList(map.keySet()).get(index));
        }
        return list;
    }

    /**
     * 根据每个几率返回随机的多个个索引 不可返回重复的
     *
     * @param num 返回适量
     * @param map k - key对象 v - prob
     * @return list.size==0失败 or随机的索引列表
     */
    public static <T> List<T> randomKeysByProbNoRepeat(Map<T, Integer> map, int num) {
        ArrayList<T> list = Lists.newArrayList();
        if (map == null) {
            return list;
        }

        ArrayList<T> keys = Lists.newArrayList(map.keySet());
        if (num >= map.size()) {
            return keys;
        }

        ArrayList<Integer> values = Lists.newArrayList(map.values());
        for (int i = 0; i < num; i++) {
            int index = randomIndexByProb(values);
            if (index == -1) {
                return list;
            }
            list.add(keys.remove(index));
            values.remove(index);
        }

        return list;
    }

    /**
     * 根据每个几率返回随机的一个索引
     *
     * @return -1失败or随机的索引
     */
    public static int randomIndexByProb(List<Integer> probs) {
        LinkedList<Integer> newProbs = new LinkedList<Integer>();
        int lastTotalProb = 0;
        for (Integer prob : probs) {
            int currentTotalProb = lastTotalProb + prob;
            newProbs.add(currentTotalProb);
            lastTotalProb = currentTotalProb;
        }
        if (newProbs.isEmpty()) {
            return -1;
        }
        int totalProb = newProbs.getLast();
        // 总概率为0
        if (totalProb == 0) {
            return -1;
        }
        int random = random(0, totalProb - 1);
        for (int i = 0; i < newProbs.size(); i++) {
            int currentTotalProb = newProbs.get(i);
            if (currentTotalProb > random) {
                return i;
            }
        }
        log.error("计算概率错误【{}】", probs.toString());
        return -1;
    }

    /**
     * 根据每个几率返回随机的一个索引
     *
     * @return -1失败or随机的索引
     */
    public static int randomIndexByProb(int[] array) {
        if (array == null || array.length == 0) {
            throw EMPTY_COLLECTION_EXCEPTION;
        }
        List<Integer> list = new ArrayList();
        for (int i : array) {
            list.add(i);
        }
        return randomIndexByProb(list);
    }

    /**
     * 从概率列表中随机一个 概率值
     *
     * @return 0则失败
     */
    public static int randomProbByIndex(List<Integer> probs) {
        if (probs == null || probs.size() <= 0) {
            return 0;
        }
        int index = random(0, probs.size() - 1);
        return probs.get(index);
    }

    private static ThreadLocal<Random> threadLocal = new ThreadLocal<Random>();

    private static Random getRandomProvider() {
        Random random = threadLocal.get();
        if (random == null) {
            String algorithm = "SHA-256";
            try {
                random = SecureRandom.getInstance(algorithm);
            } catch (NoSuchAlgorithmException e) {
                random = new SecureRandom();
            }
            threadLocal.set(random);
        }
        return random;
    }

    /**
     * 获得开奖随机号
     *
     * @return
     */
    public static int geRandomNumt() {
        return getRandomProvider().nextInt(10);
    }

    public static int nextInt(int num) {
        return getRandomProvider().nextInt(num);
    }
}


package com.sugar.common.util;

import com.sugar.server.module.player.SessionManager;
import io.netty.util.internal.PlatformDependent;
import lombok.extern.slf4j.Slf4j;
import sun.misc.JavaNioAccess;

import java.lang.management.BufferPoolMXBean;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.reflect.Field;
import java.util.concurrent.atomic.AtomicLong;
import sun.misc.SharedSecrets;

/**
 * @author author
 */
@Slf4j
public class StatusUtil {

    public static BufferPoolMXBean getDirectBufferPoolMBean() {
        return ManagementFactory.getPlatformMXBeans(BufferPoolMXBean.class)
                .stream()
                .filter(e -> e.getName().equals("direct"))
                .findFirst()
                .orElseThrow(() -> new RuntimeException());
    }

    public static JavaNioAccess.BufferPool getNioBufferPool() {
        return SharedSecrets.getJavaNioAccess().getDirectBufferPool();
    }

    public static void jvmInfo(){
        try {
            Class c1 = Class.forName("io.netty.util.internal.PlatformDependent");

            Field DIRECT_MEMORY_LIMIT = c1.getDeclaredField("DIRECT_MEMORY_LIMIT");
            DIRECT_MEMORY_LIMIT.setAccessible(true);
            log.info("最大堆外内存限制：" + (long) DIRECT_MEMORY_LIMIT.get(PlatformDependent.class)
                    / 1024 / 1024);

            Field field1 = c1.getDeclaredField("DIRECT_MEMORY_COUNTER");
            field1.setAccessible(true);
            AtomicLong directMemory  = (AtomicLong) field1.get(PlatformDependent.class);


            int memoryInKb = (int) (directMemory.get() / 1024);
            log.info("netty堆外内存使用: " + memoryInKb + " kB");
            log.info("--------------------------------------");
            //log.info("连接数：\t" + futureMap.size());
            log.info("jvm最大内存：\t" + Runtime.getRuntime().maxMemory() / 1024 / 1024 + "M");
            log.info("jvm总内存：\t" + Runtime.getRuntime().totalMemory() / 1024 / 1024 + "M");
            log.info("jvm可用：\t" + Runtime.getRuntime().freeMemory() / 1024 / 1024 + "M");
            MemoryMXBean m = ManagementFactory.getMemoryMXBean();
            long committed = m.getNonHeapMemoryUsage().getCommitted();
            long init = m.getNonHeapMemoryUsage().getInit();
            long used = m.getNonHeapMemoryUsage().getUsed();
            long max = m.getNonHeapMemoryUsage().getMax();
            log.info("No-Heap:");
            log.info("  --init:\t" + init / 1024 / 1024 + "M");
            log.info("  --committed:\t" + committed / 1024 / 1024 + "M");
            log.info("  --used:\t" + used / 1024 / 1024 + "M");
            log.info("  --max:\t" + max / 1024 / 1024 + "M");

            log.info("Heap:");
            log.info("  --init:\t" + m.getHeapMemoryUsage().getInit() / 1024 / 1024 + "M");
            log.info("  --committed:\t" + m.getHeapMemoryUsage().getCommitted() / 1024 / 1024 + "M");
            log.info("  --used:\t" + m.getHeapMemoryUsage().getUsed() / 1024 / 1024 + "M");
            log.info("  --max:\t" + m.getHeapMemoryUsage().getMax() / 1024 / 1024 + "M");


            BufferPoolMXBean directBufferPoolMBean = getDirectBufferPoolMBean();
            log.info("DirectBufferPool:");
            log.info("  --MemoryUsed:\t" + directBufferPoolMBean.getMemoryUsed() / 1024 / 1024 + "M");
            log.info("  --TotalCapacity:\t" + directBufferPoolMBean.getTotalCapacity() / 1024 / 1024 + "M");
            log.info("  --Count:\t" + directBufferPoolMBean.getCount());

            JavaNioAccess.BufferPool nioBufferPool = getNioBufferPool();
            log.info("NioBufferPool:");
            log.info("  --MemoryUsed:\t" + nioBufferPool.getMemoryUsed());
            log.info("  --TotalCapacity:\t" + nioBufferPool.getTotalCapacity());
            log.info("  --Count:\t" + nioBufferPool.getCount());
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void serverInfo(){
        log.info("--------------------------------------");
        log.info("当前在线用户: " + SessionManager.MAP_SESSION.size());
    }
}

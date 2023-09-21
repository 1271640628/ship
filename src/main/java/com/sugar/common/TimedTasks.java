package com.sugar.common;

import com.sugar.basedb.ConfigCache;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author jy lai
 */
@Slf4j
public class TimedTasks {
    public static Scheduler scheduler;
    static Map<String, Class> task;

    /**
     * key_1:cron 表达式，key_2：执行的class
     *
     * @throws SchedulerException
     * @throws InterruptedException
     */
    public static void addJob() throws SchedulerException {
        task = new HashMap<>();
        task.put("0 0/5 * * * ? *", ConfigCache.class);
        //task.put("10 * * * * ? ", testHandle.class);
        AtomicLong atomicLong = new AtomicLong();
        SchedulerFactory factory = new StdSchedulerFactory();
        scheduler = factory.getScheduler();

        for (String key : task.keySet()) {
            long taskId = atomicLong.getAndIncrement();
            JobDetail job = JobBuilder.newJob(task.get(key))
                    .withIdentity("job" + taskId, "group" + taskId).build();

            CronTrigger trigger = TriggerBuilder.newTrigger()
                    .withIdentity("trigger" + taskId, "group" + taskId)
                    .startAt(new Date())
                    .withSchedule(CronScheduleBuilder.cronSchedule(key))
                    .build();
            scheduler.scheduleJob(job, trigger);
        }
        scheduler.start();
    }
}

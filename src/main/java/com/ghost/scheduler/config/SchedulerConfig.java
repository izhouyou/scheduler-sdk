package com.ghost.scheduler.config;

import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <p>Description: [配置Quartz的Scheduler对象]</p>
 * @version 1.0
 */
@Configuration
public class SchedulerConfig {

    /**
     * Discription:[方Quartz获取Scheduler对象单例]
     */
    @Bean(name="scheduler")
    public Scheduler getScheduler() {
        Scheduler scheduler = null;
        try {
            scheduler = StdSchedulerFactory.getDefaultScheduler();
        } catch (SchedulerException se) {
            se.printStackTrace();
        }
        return scheduler;
    }

}

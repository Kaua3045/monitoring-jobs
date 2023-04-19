package com.kaua.monitoring.jobs.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

@Configuration
public class ThreadPoolTaskConfiguration {

    private final String THREAD_NAME_PREFIX_JOB = "Job-Scheduler";

    @Bean
    public ThreadPoolTaskScheduler threadPoolTaskScheduler() {
        final var threadPoolTask = new ThreadPoolTaskScheduler();
        threadPoolTask.setPoolSize(4);
        threadPoolTask.setThreadNamePrefix(THREAD_NAME_PREFIX_JOB);
        threadPoolTask.initialize();
        return threadPoolTask;
    }
}

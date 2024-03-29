package com.kaua.monitoring.jobs.configurations;

import com.kaua.monitoring.jobs.links.JobLinks;
import com.kaua.monitoring.jobs.readers.*;
import com.kaua.monitoring.jobs.runnables.RunnableJob;
import jakarta.annotation.PostConstruct;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
public class StartJobs {

    @Autowired
    private ThreadPoolTaskScheduler taskScheduler;

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private JobLinks jobLinks;

    @Autowired
    private LinksJobReader linksJobReader;

    @Autowired
    private NoRepeatJobReader noRepeatJobReader;

    @Autowired
    private OnSpecificDayJobReader specificDayJobReader;

    @Autowired
    private TwoTimesAMonthJobReader twoTimesAMonthJobReader;

    @Autowired
    private EveryDayJobReader everyDayJobReader;

    @Autowired
    private EveryFiveHoursJobReader everyFiveHoursJobReader;

    @Autowired
    private ValidateNextExecuteDateJobReader validateNextExecuteDateJobReader;

    @PostConstruct
    public void fetchUrlJobExecution() {
        taskScheduler.scheduleWithFixedDelay(
                new RunnableJob(
                        jobLauncher,
                        jobLinks.fetchUrlsJob(
                                linksJobReader,
                                validateNextExecuteDateJobReader
                        )),
                Duration.ofMinutes(1));
    }
}

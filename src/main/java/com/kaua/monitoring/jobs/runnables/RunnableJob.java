package com.kaua.monitoring.jobs.runnables;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;

import java.util.Objects;
import java.util.UUID;

public class RunnableJob implements Runnable {

    private final JobLauncher jobLauncher;
    private final Job job;

    public RunnableJob(final JobLauncher jobLauncher, final Job job) {
        this.jobLauncher = Objects.requireNonNull(jobLauncher);
        this.job = Objects.requireNonNull(job);
    }

    @Override
    public void run() {
        try {
            final var jobParameters = new JobParametersBuilder()
                    .addString("job-id", UUID.randomUUID().toString())
                            .toJobParameters();

            jobLauncher.run(job, jobParameters);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

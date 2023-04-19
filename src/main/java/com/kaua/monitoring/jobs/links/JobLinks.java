package com.kaua.monitoring.jobs.links;

import com.kaua.monitoring.jobs.readers.*;
import com.kaua.monitoring.jobs.readers.outputs.LinkJobOutput;
import com.kaua.monitoring.jobs.services.gateways.MessengerGateway;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.adapter.ItemWriterAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.UUID;

@Configuration
public class JobLinks {

    private final static int VALUE_PER_CHUNK = 20;

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private PlatformTransactionManager transactionManager;

    @Autowired
    private MessengerGateway messengerGateway;

    public Job fetchUrlsJob(
            NoRepeatJobReader noRepeatJobReader,
            OnSpecificDayJobReader onSpecificDayJobReader,
            TwoTimesAMonthJobReader twoTimesAMonthJobReader,
            EveryDayJobReader everyDayJobReader,
            EveryFiveHoursJobReader everyFiveHoursJobReader
    ) {
        return new JobBuilder("fetch-urls-job", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(noRepeatStep(noRepeatJobReader))
                .next(onSpecificDayStep(onSpecificDayJobReader))
                .next(twoTimesAMonthStep(twoTimesAMonthJobReader))
                .next(everyDayStep(everyDayJobReader))
                .next(everyFiveHoursStep(everyFiveHoursJobReader))
                .build();
    }

    @Bean
    public ItemWriter<LinkJobOutput> writer() {
        final var itemWriterAdapter = new ItemWriterAdapter<LinkJobOutput>();
        itemWriterAdapter.setTargetObject(messengerGateway);
        itemWriterAdapter.setTargetMethod("sendMessage");
        return itemWriterAdapter;
    }

    @Bean
    @Qualifier("noRepeatStep")
    public Step noRepeatStep(
            NoRepeatJobReader noRepeatJobReader
    ) {
        return new StepBuilder("check-links-no-repeat-" + UUID.randomUUID(), jobRepository)
                .<LinkJobOutput, LinkJobOutput>chunk(VALUE_PER_CHUNK, transactionManager)
                .reader(noRepeatJobReader.noRepeatReader())
                .writer(writer())
                .build();
    }

    @Bean
    @Qualifier("onSpecificDayStep")
    public Step onSpecificDayStep(
            OnSpecificDayJobReader specificDayJobReader
    ) {
        return new StepBuilder("check-links-specific-day-" + UUID.randomUUID(), jobRepository)
                .<LinkJobOutput, LinkJobOutput>chunk(VALUE_PER_CHUNK, transactionManager)
                .reader(specificDayJobReader.specificDayReader())
                .writer(writer())
                .build();
    }

    @Bean
    @Qualifier("twoTimesAMonthStep")
    public Step twoTimesAMonthStep(
            TwoTimesAMonthJobReader twoTimesAMonthJobReader
    ) {
        return new StepBuilder("check-links-two-times-month-" + UUID.randomUUID(), jobRepository)
                .<LinkJobOutput, LinkJobOutput>chunk(VALUE_PER_CHUNK, transactionManager)
                .reader(twoTimesAMonthJobReader.twoTimesMonthReader())
                .writer(writer())
                .build();
    }

    @Bean
    @Qualifier("everyDayStep")
    public Step everyDayStep(
            EveryDayJobReader everyDayJobReader
    ) {
        return new StepBuilder("check-links-every-day-" + UUID.randomUUID(), jobRepository)
                .<LinkJobOutput, LinkJobOutput>chunk(VALUE_PER_CHUNK, transactionManager)
                .reader(everyDayJobReader.everyDayReader())
                .writer(writer())
                .build();
    }

    @Bean
    @Qualifier("everyFiveHoursStep")
    public Step everyFiveHoursStep(
            EveryFiveHoursJobReader EveryFiveHoursJobReader
    ) {
        return new StepBuilder("check-links-every-five-hours-" + UUID.randomUUID(), jobRepository)
                .<LinkJobOutput, LinkJobOutput>chunk(VALUE_PER_CHUNK, transactionManager)
                .reader(EveryFiveHoursJobReader.everyFiveHoursReader())
                .writer(writer())
                .build();
    }
}

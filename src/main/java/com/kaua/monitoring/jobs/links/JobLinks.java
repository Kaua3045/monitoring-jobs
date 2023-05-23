package com.kaua.monitoring.jobs.links;

import com.kaua.monitoring.jobs.readers.LinksJobReader;
import com.kaua.monitoring.jobs.readers.ValidateNextExecuteDateJobReader;
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
            LinksJobReader linksJobReader,
            ValidateNextExecuteDateJobReader validateNextExecuteDateJobReader
    ) {
        return new JobBuilder("fetch-urls-job", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(linksJobReaderStep(linksJobReader))
                .next(validateAllNextExecuteDateStep(
                        validateNextExecuteDateJobReader
                ))
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
    @Qualifier("CheckLinks")
    public Step linksJobReaderStep(LinksJobReader linksJobReader) {
        return new StepBuilder("check-links-to-next-execute-date-" + UUID.randomUUID(), jobRepository)
                .<LinkJobOutput, LinkJobOutput>chunk(VALUE_PER_CHUNK, transactionManager)
                .reader(linksJobReader.findAllLinksJobReader())
                .writer(writer())
                .build();
    }

    @Bean
    @Qualifier("ValidateNextExecuteDateStep")
    public Step validateAllNextExecuteDateStep(
            @Qualifier("validateNextExecuteDateJobReader") ValidateNextExecuteDateJobReader validateNextExecuteDate
    ) {
        return new StepBuilder("validate-next-execute-date-" + UUID.randomUUID(), jobRepository)
                .<LinkJobOutput, LinkJobOutput>chunk(VALUE_PER_CHUNK, transactionManager)
                .reader(validateNextExecuteDate.validateNextExecuteDateJob())
                .writer(writer())
                .build();
    }
}

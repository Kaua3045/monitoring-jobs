package com.kaua.monitoring.jobs.readers;

import com.kaua.monitoring.jobs.readers.outputs.LinkJobOutput;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

import javax.sql.DataSource;

@Configuration
public class EveryDayJobReader {

    private static final String EVERY_DAY_JOB_READER_NAME = "find-all-with-every-day";

    private static final String SQL_FIND_ALL_WITH_EVERY_DAY = "SELECT id, url FROM links " +
            "WHERE (link_execution = 'EVERY_DAYS' " +
            "AND date_trunc('minutes', execute_date) = date_trunc('minutes', current_timestamp));";

    @Autowired
    private DataSource dataSource;

    @Bean
    public JdbcCursorItemReader<LinkJobOutput> everyDayReader() {
        return new JdbcCursorItemReaderBuilder<LinkJobOutput>()
                .name(EVERY_DAY_JOB_READER_NAME)
                .dataSource(dataSource)
                .sql(SQL_FIND_ALL_WITH_EVERY_DAY)
                .rowMapper(new BeanPropertyRowMapper<>(LinkJobOutput.class))
                .build();
    }
}

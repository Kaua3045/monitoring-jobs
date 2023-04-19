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
public class EveryFiveHoursJobReader {

    private static final String EVERY_FIVE_HOURS_JOB_READER_NAME = "find-all-with-every-five-hours";

    private static final String SQL_FIND_ALL_WITH_EVERY_FIVE_HOURS = "SELECT * FROM links " +
            "WHERE link_execution = 'EVERY_FIVE_HOURS' " +
            "AND extract(minute FROM execute_date) = extract(minute FROM current_timestamp) " +
            "AND extract(hour FROM execute_date) % 5 = extract(hour FROM current_timestamp) % 5 " +
            "AND execute_date BETWEEN current_timestamp AND current_timestamp + interval '3 minutes'";

    @Autowired
    private DataSource dataSource;

    @Bean
    public JdbcCursorItemReader<LinkJobOutput> everyFiveHoursReader() {
        return new JdbcCursorItemReaderBuilder<LinkJobOutput>()
                .name(EVERY_FIVE_HOURS_JOB_READER_NAME)
                .dataSource(dataSource)
                .sql(SQL_FIND_ALL_WITH_EVERY_FIVE_HOURS)
                .rowMapper(new BeanPropertyRowMapper<>(LinkJobOutput.class))
                .build();
    }
}

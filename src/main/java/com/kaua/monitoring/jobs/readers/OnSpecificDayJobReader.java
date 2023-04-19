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
public class OnSpecificDayJobReader {

    private static final String ON_SPECIFIC_DAY_JOB_READER_NAME = "find-all-with-on-specific-day";

    private static final String SQL_FIND_ALL_WITH_ON_SPECIFIC_DAY = "SELECT id, url FROM links " +
            "WHERE (link_execution = 'ON_SPECIFIC_DAY' AND date_trunc('day', execute_date) = " +
            "date_trunc('day', current_timestamp) AND execute_date BETWEEN current_timestamp " +
            "AND current_timestamp + '1 minutes'::interval);";

    @Autowired
    private DataSource dataSource;

    @Bean
    public JdbcCursorItemReader<LinkJobOutput> specificDayReader() {
        return new JdbcCursorItemReaderBuilder<LinkJobOutput>()
                .name(ON_SPECIFIC_DAY_JOB_READER_NAME)
                .dataSource(dataSource)
                .sql(SQL_FIND_ALL_WITH_ON_SPECIFIC_DAY)
                .rowMapper(new BeanPropertyRowMapper<>(LinkJobOutput.class))
                .build();
    }
}

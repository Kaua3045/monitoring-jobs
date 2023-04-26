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
public class NoRepeatJobReader {

    private static final String NO_REPEAT_JOB_READER_NAME = "find-all-with-no-repeat";

    private static final String SQL_FIND_ALL_WITH_NO_REPEAT = "SELECT id, url FROM links " +
            "WHERE (link_execution = 'NO_REPEAT' AND execute_date BETWEEN current_timestamp " +
            "AND current_timestamp + '1 minutes'::interval);";

    @Autowired
    private DataSource dataSource;

    @Bean
    public JdbcCursorItemReader<LinkJobOutput> noRepeatReader() {
        return new JdbcCursorItemReaderBuilder<LinkJobOutput>()
                .name(NO_REPEAT_JOB_READER_NAME)
                .dataSource(dataSource)
                .sql(SQL_FIND_ALL_WITH_NO_REPEAT)
                .rowMapper(new BeanPropertyRowMapper<>(LinkJobOutput.class))
                .build();
    }
}

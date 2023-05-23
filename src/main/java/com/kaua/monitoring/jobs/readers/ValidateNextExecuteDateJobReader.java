package com.kaua.monitoring.jobs.readers;

import com.kaua.monitoring.jobs.readers.outputs.LinkJobOutput;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

import javax.sql.DataSource;

@Configuration
public class ValidateNextExecuteDateJobReader {

    private static final String VALIDATE_LINKS_JOB_READER_NAME = "find-all-links-with-next-execute-date-is-before";

    private static final String SQL_FIND_ALL_WITH_NEXT_EXECUTE_DATE_IS_BEFORE = "SELECT id, url, link_execution, next_execute_date " +
            "FROM links " +
            "WHERE next_execute_date IS NOT NULL AND next_execute_date < current_timestamp - interval '10 minutes'";

    @Autowired
    private DataSource dataSource;

    @Bean
    public JdbcCursorItemReader<LinkJobOutput> validateNextExecuteDateJob() {
        return new JdbcCursorItemReaderBuilder<LinkJobOutput>()
                .name(VALIDATE_LINKS_JOB_READER_NAME)
                .dataSource(dataSource)
                .sql(SQL_FIND_ALL_WITH_NEXT_EXECUTE_DATE_IS_BEFORE)
                .rowMapper(new BeanPropertyRowMapper<>(LinkJobOutput.class))
                .build();

    }
}

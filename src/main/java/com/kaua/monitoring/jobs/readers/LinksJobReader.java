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
public class LinksJobReader {

    @Autowired
    private DataSource dataSource;

    private static final String LINKS_JOB_READER_NAME = "find-all-links-to-execute";

    private static final String SQL_FIND_ALL_WITH_NEXT_EXECUTE_DATE = "SELECT id, url, link_execution " +
            "FROM links " +
            "WHERE next_execute_date IS NOT NULL " +
            "AND extract(minute FROM next_execute_date) = extract(minute FROM current_timestamp) " +
            "AND extract(hour FROM next_execute_date) = extract(hour FROM current_timestamp) " +
            "AND extract(day FROM next_execute_date) = extract(day FROM current_timestamp) " +
            "AND extract(month FROM next_execute_date) = extract(month FROM current_timestamp)";

    @Bean
    public JdbcCursorItemReader<LinkJobOutput> findAllLinksJobReader() {
        return new JdbcCursorItemReaderBuilder<LinkJobOutput>()
                .name(LINKS_JOB_READER_NAME)
                .dataSource(dataSource)
                .sql(SQL_FIND_ALL_WITH_NEXT_EXECUTE_DATE)
                .rowMapper(new BeanPropertyRowMapper<>(LinkJobOutput.class))
                .build();
    }
}

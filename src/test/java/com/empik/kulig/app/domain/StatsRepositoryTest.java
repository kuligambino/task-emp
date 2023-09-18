package com.empik.kulig.app.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;

import static org.junit.jupiter.api.Assertions.assertEquals;

@JdbcTest
@Sql({"classpath:test-data.sql"})
@Import(StatsRepository.class)
class StatsRepositoryTest {

    @Autowired
    private StatsRepository statsRepository;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void whenLoginExistsThenIncrementRequestCount() {
        // given
        String login = "test";
        String getRequestCountSql = "SELECT request_count FROM api_request_stats WHERE login = ?";
        Integer currentCount = jdbcTemplate.queryForObject(getRequestCountSql, Integer.class, login);

        // when
        statsRepository.incrementRequestCount(login);

        // then
        Integer updatedCount = jdbcTemplate.queryForObject(getRequestCountSql, Integer.class, login);
        assertEquals(currentCount + 1, updatedCount);
    }

    @Test
    void whenLoginNotFoundThenAddWith1RequestCount() {
        // given
        String notExistingLogin = "I don't exist";
        String getRequestCountSql = "SELECT request_count FROM api_request_stats WHERE login = ?";

        // when
        statsRepository.incrementRequestCount(notExistingLogin);

        // then
        Integer currentCount = jdbcTemplate.queryForObject(getRequestCountSql, Integer.class, notExistingLogin);
        assertEquals(1, currentCount);
    }
}

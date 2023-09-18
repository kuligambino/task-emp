package com.empik.kulig.app.domain;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
@Slf4j
class StatsRepository {

    private static final int STARTING_VALUE = 1;

    private final JdbcTemplate jdbcTemplate;

    void incrementRequestCount(String login) {
        try {
            int currentCount = jdbcTemplate.queryForObject("SELECT request_count FROM api_request_stats WHERE login = ?", Integer.class, login);
            log.info("Updating request_count for user: [{}] from {} to {}.", login, currentCount, currentCount+1);
            updateRequestCount(login, currentCount + 1);
        } catch (EmptyResultDataAccessException ex) {
            log.info("First api call for user: [{}]. Creating new record...", login);
            jdbcTemplate.update("INSERT INTO api_request_stats (login, request_count) VALUES (?, ?)", login, STARTING_VALUE);
        }
    }

    private void updateRequestCount(String login, int currentCount) {
        jdbcTemplate.update("UPDATE api_request_stats SET request_count = ? WHERE login = ?", currentCount, login);
    }
}

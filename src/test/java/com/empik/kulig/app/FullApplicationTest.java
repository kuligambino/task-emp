package com.empik.kulig.app;

import com.empik.kulig.app.domain.StatsService;
import com.empik.kulig.app.dto.UserStatsDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.reactive.server.WebTestClient;

import static com.empik.kulig.app.UserStatsAssert.assertThatStats;
import static com.empik.kulig.app.UserStatsFactory.userStats;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@Sql({"classpath:test-data.sql"})
class FullApplicationTest {

    @LocalServerPort
    private Integer port;
    @Autowired
    private WebTestClient webTestClient;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    private final int[] currentFollowers = {0};
    private final int[] currentPublicRepos = {0};

    @Test
    void fullFlowTest() {
        // given
        String login = "octocat";
        getCurrentFollowersAndPublicRepos(login);
        int currentCount = getRequestCount(login);

        // when then
        webTestClient.get()
                .uri("http://localhost:" + port + "/{login}", login)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(UserStatsDto.class)
                .value(userStatsResponse -> assertThatStats(userStatsResponse).isEqualToUserStats(userStats(login, currentFollowers[0], currentPublicRepos[0])));


        int updatedCount = getRequestCount(login);
        assertEquals(currentCount + 1, updatedCount);
    }

    private void getCurrentFollowersAndPublicRepos(String login) {
        webTestClient.get().uri("https://api.github.com/users/{login}", login).exchange()
                .expectBody()
                .jsonPath("$.followers").value(followersResponse -> {
                    currentFollowers[0] = (int) followersResponse;
                })
                .jsonPath("$.public_repos").value(publicReposResponse -> {
                    currentPublicRepos[0] = (int) publicReposResponse;
                });
    }

    private int getRequestCount(String login) {
        String getRequestCountSql = "SELECT COALESCE(request_count, 0) FROM api_request_stats WHERE login = ?";
        try {
            return jdbcTemplate.queryForObject(getRequestCountSql, Integer.class, login);
        } catch (EmptyResultDataAccessException e) {
            return 0;
        }
    }
}

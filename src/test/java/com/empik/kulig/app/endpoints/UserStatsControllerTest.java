package com.empik.kulig.app.endpoints;

import com.empik.kulig.app.domain.StatsService;
import com.empik.kulig.app.domain.UserNotExistException;
import com.empik.kulig.app.dto.UserStatsDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

import static com.empik.kulig.app.UserStatsAssert.assertThatStats;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@WebFluxTest(UserStatsController.class)
class UserStatsControllerTest {

    @Autowired
    private WebTestClient webClient;
    @MockBean
    private StatsService statsService;


    @Test
    void shouldReturn200AndCorrectBody() {
        // given
        String login = "test";
        UserStatsDto expectedUserStatsDto = new UserStatsDto(123, "test", "Test User", "USER",
                "https://avatars.test.com/u/123", LocalDateTime.now(), 1.0);
        Mono<UserStatsDto> userStatsDto = Mono.just(expectedUserStatsDto);

        // when
        when(statsService.getUserStats(login)).thenReturn(userStatsDto);

        // then
        webClient.get()
                .uri("/{login}", login)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(UserStatsDto.class)
                .value(userStatsResponse -> assertThatStats(userStatsResponse).isEqualToUserStats(expectedUserStatsDto));
    }

    @Test
    void shouldReturn404IfLoginDoesntExist() {
        // given
        String notExistingUser = "I don't exist";

        // when
        when(statsService.getUserStats(notExistingUser)).thenThrow(new UserNotExistException(notExistingUser));

        // then
        webClient.get()
                .uri("/{login}", notExistingUser)
                .exchange()
                .expectStatus()
                .isEqualTo(NOT_FOUND);
    }

}

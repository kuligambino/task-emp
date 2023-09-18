package com.empik.kulig.app.domain;

import com.empik.kulig.app.dto.UserDto;
import com.empik.kulig.app.dto.UserStatsDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static reactor.core.publisher.Mono.error;

@Service
@RequiredArgsConstructor
@Slf4j
public class StatsService {

    private final StatsRepository statsRepository;
    private final WebClient webClient;

    public Mono<UserStatsDto> getUserStats(String login) {
        log.info("Start fetching data for user [{}]", login);
        return webClient.get()
                .uri("/users/{login}", login)
                .retrieve()
                .onStatus(status -> status.isSameCodeAs(NOT_FOUND), ex -> error(new UserNotExistException(login)))
                .bodyToMono(UserDto.class)
                .doOnSuccess(userStatsDto -> log.info("Successfully fetched data: [{}]", userStatsDto))
                .map(UserStatsDto::new)
                .doOnSuccess(userStatsDto -> statsRepository.incrementRequestCount(login));
    }
}

package com.empik.kulig.app.domain;

import com.empik.kulig.app.dto.UserStatsDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class StatsService {

    private final StatsRepository statsRepository;
    private final UserStatsClient userStatsClient;

    public Mono<UserStatsDto> getUserStats(String login) {
        log.info("Start fetching data for user [{}]", login);
        return userStatsClient.getUser(login)
                .map(UserStatsDto::new)
                .doOnSuccess(userStatsDto -> statsRepository.incrementRequestCount(login));
    }
}

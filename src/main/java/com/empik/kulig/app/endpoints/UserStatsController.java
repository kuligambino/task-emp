package com.empik.kulig.app.endpoints;

import com.empik.kulig.app.domain.StatsService;
import com.empik.kulig.app.dto.UserStatsDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequiredArgsConstructor
class UserStatsController {

    private final StatsService statsService;

    @GetMapping("{login}")
    ResponseEntity<Mono<UserStatsDto>> getUserStats(@PathVariable String login) {
        return ok(statsService.getUserStats(login));
    }
}

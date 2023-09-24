package com.empik.kulig.app.domain;

import com.empik.kulig.app.dto.UserDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.FileCopyUtils;
import reactor.test.StepVerifier;

import java.io.IOException;

import static com.empik.kulig.app.UserStatsAssert.assertThatStats;
import static org.mockito.Mockito.*;
import static reactor.core.publisher.Mono.just;

@ExtendWith(MockitoExtension.class)
class StatsServiceTest {

    private StatsService statsService;
    private StatsRepository statsRepository;
    private UserStatsClient userStatsClient;
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        userStatsClient = mock(UserStatsClient.class);
        statsRepository = mock(StatsRepository.class);
        statsService = new StatsService(statsRepository, userStatsClient);
        objectMapper = new ObjectMapper().findAndRegisterModules();
    }

    @Test
    void whenGetUserThenReturnAllUserStats() throws IOException {
        // given
        String login = "test";
        String body = readFile("user.json");
        UserDto userDto = objectMapper.readValue(body, UserDto.class);
        when(userStatsClient.getUser(login)).thenReturn(just(userDto));

        // when then
        StepVerifier.create(statsService.getUserStats(login))
                .expectNextMatches(userStats -> {
                    assertThatStats(userStats).isEqualToUserAndCalculations(userDto, 7.0);
                    verify(statsRepository, times(1)).incrementRequestCount(login);
                    return true;
                })
                .expectComplete()
                .verify();
    }

    @Test
    void whenUserHas0FollowersThenCalculationsEquals0() throws IOException {
        // given
        String noFollowersLogin = "noFollowersLogin";
        String body = readFile("user_0_followers.json");
        UserDto userDto = objectMapper.readValue(body, UserDto.class);
        when(userStatsClient.getUser(noFollowersLogin)).thenReturn(just(userDto));

        // when then
        StepVerifier.create(statsService.getUserStats(noFollowersLogin))
                .expectNextMatches(userStats -> {
                    assertThatStats(userStats).isEqualToUserAndCalculations(userDto, 0);
                    verify(statsRepository, times(1)).incrementRequestCount(noFollowersLogin);
                    return true;
                })
                .expectComplete()
                .verify();
    }

    private String readFile(String resourcePath) throws IOException {
        ClassPathResource resource = new ClassPathResource(resourcePath);
        byte[] json = FileCopyUtils.copyToByteArray(resource.getInputStream());
        return new String(json);
    }
}

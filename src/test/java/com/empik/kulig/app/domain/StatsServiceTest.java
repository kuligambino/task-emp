package com.empik.kulig.app.domain;

import com.empik.kulig.app.dto.UserDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.test.StepVerifier;

import java.io.IOException;

import static com.empik.kulig.app.domain.UserStatsAssert.assertThatStats;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;

@ExtendWith(MockitoExtension.class)
class StatsServiceTest {

    @InjectMocks
    private StatsService statsService;
    @Mock
    private StatsRepository statsRepository;
    private MockWebServer mockWebServer;
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        mockWebServer = new MockWebServer();
        WebClient webClient = WebClient.builder()
                .baseUrl(mockWebServer.url("/").toString())
                .build();
        statsService = new StatsService(statsRepository, webClient);
        objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
    }

    @AfterEach
    public void tearDown() throws Exception {
        mockWebServer.shutdown();
    }

    @Test
    void whenGetUserThenReturnAllUserStats() throws IOException {
        // given
        String login = "test";
        String body = enqueueResponse("user.json");
        UserDto userDto = objectMapper.readValue(body, UserDto.class);

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
        String body = enqueueResponse("user_0_followers.json");
        UserDto userDto = objectMapper.readValue(body, UserDto.class);

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

    @Test
    void whenUserNameNotFoundThen404() {
        // given
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(NOT_FOUND.value()));

        // when then
        StepVerifier.create(statsService.getUserStats("I don't exist"))
                .expectErrorSatisfies(throwable -> {
                    assertTrue(throwable instanceof UserNotExistException);
                    verify(statsRepository, times(0)).incrementRequestCount("test");
                })
                .verify();
    }

    private String enqueueResponse(String resourcePath) throws IOException {
        ClassPathResource resource = new ClassPathResource(resourcePath);
        byte[] json = FileCopyUtils.copyToByteArray(resource.getInputStream());
        String body = new String(json);

        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(OK.value())
                .setHeader("Content-Type", "application/json")
                .setBody(body));

        return body;
    }
}

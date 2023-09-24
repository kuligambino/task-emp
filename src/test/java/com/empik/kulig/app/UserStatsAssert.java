package com.empik.kulig.app;

import com.empik.kulig.app.dto.UserDto;
import com.empik.kulig.app.dto.UserStatsDto;
import lombok.RequiredArgsConstructor;

import static org.assertj.core.api.Assertions.assertThat;

@RequiredArgsConstructor
public class UserStatsAssert {
    private final UserStatsDto userStatsDto;

    public static UserStatsAssert assertThatStats(UserStatsDto userStats) {
        return new UserStatsAssert(userStats);
    }

    public UserStatsAssert isEqualToUserAndCalculations(UserDto inputUser, double expectedCalculations) {
        assertThat(userStatsDto.id()).isEqualTo(inputUser.id());
        assertThat(userStatsDto.name()).isEqualTo(inputUser.name());
        assertThat(userStatsDto.type()).isEqualTo(inputUser.type());
        assertThat(userStatsDto.avatarUrl()).isEqualTo(inputUser.avatarUrl());
        assertThat(userStatsDto.createdAt()).isEqualTo(inputUser.createdAt());
        assertThat(userStatsDto.calculations()).isEqualTo(expectedCalculations);
        return this;
    }

    public UserStatsAssert isEqualToUserStats(UserStatsDto userStats) {
        assertThat(userStatsDto.id()).isEqualTo(userStats.id());
        assertThat(userStatsDto.name()).isEqualTo(userStats.name());
        assertThat(userStatsDto.type()).isEqualTo(userStats.type());
        assertThat(userStatsDto.avatarUrl()).isEqualTo(userStats.avatarUrl());
        assertThat(userStatsDto.createdAt()).isEqualTo(userStats.createdAt());
        assertThat(userStatsDto.calculations()).isEqualTo(userStats.calculations());
        return this;
    }
}

package com.empik.kulig.app.domain;

import com.empik.kulig.app.dto.UserDto;
import com.empik.kulig.app.dto.UserStatsDto;
import lombok.RequiredArgsConstructor;

import static org.assertj.core.api.Assertions.assertThat;

@RequiredArgsConstructor
class UserStatsAssert {
    private final UserStatsDto userStatsDto;

    static UserStatsAssert assertThatStats(UserStatsDto userStats) {
        return new UserStatsAssert(userStats);
    }

    UserStatsAssert isEqualToUserAndCalculations(UserDto inputUser, double expectedCalculations) {
        assertThat(userStatsDto.id()).isEqualTo(inputUser.id());
        assertThat(userStatsDto.name()).isEqualTo(inputUser.name());
        assertThat(userStatsDto.type()).isEqualTo(inputUser.type());
        assertThat(userStatsDto.avatarUrl()).isEqualTo(inputUser.avatarUrl());
        assertThat(userStatsDto.createdAt()).isEqualTo(inputUser.createdAt());
        assertThat(userStatsDto.calculations()).isEqualTo(expectedCalculations);
        return this;
    }
}

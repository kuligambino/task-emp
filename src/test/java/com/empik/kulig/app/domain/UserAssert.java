package com.empik.kulig.app.domain;

import com.empik.kulig.app.dto.UserDto;
import lombok.RequiredArgsConstructor;

import static org.assertj.core.api.Assertions.assertThat;

@RequiredArgsConstructor
class UserAssert {
    private final UserDto user;

    static UserAssert assertThatStats(UserDto userDto) {
        return new UserAssert(userDto);
    }

    UserAssert isEqualToUser(UserDto userDto) {
        assertThat(user.id()).isEqualTo(userDto.id());
        assertThat(user.name()).isEqualTo(userDto.name());
        assertThat(user.type()).isEqualTo(userDto.type());
        assertThat(user.avatarUrl()).isEqualTo(userDto.avatarUrl());
        assertThat(user.createdAt()).isEqualTo(userDto.createdAt());
        assertThat(user.followers()).isEqualTo(userDto.followers());
        assertThat(user.publicReposNumber()).isEqualTo(userDto.publicReposNumber());
        return this;
    }
}

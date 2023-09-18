package com.empik.kulig.app.dto;

import java.time.LocalDateTime;

public record UserStatsDto(long id, String login, String name, String type, String avatarUrl, LocalDateTime createdAt,
                           double calculations) {
    public UserStatsDto(UserDto dto) {
        this(dto.id(), dto.login(), dto.name(), dto.type(), dto.avatarUrl(), dto.createdAt(),
                dto.followers() != 0 ? 6.0 / dto.followers() * (2 + dto.publicReposNumber()) : 0);
    }
}

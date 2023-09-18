package com.empik.kulig.app.dto;


import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

public record UserDto(long id, String login, String name, String type, @JsonProperty("avatar_url") String avatarUrl,
                      @JsonProperty("created_at") LocalDateTime createdAt, int followers,
                      @JsonProperty("public_repos") int publicReposNumber) {
}

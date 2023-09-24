package com.empik.kulig.app;

import com.empik.kulig.app.dto.UserStatsDto;

import java.time.format.DateTimeFormatter;

import static java.time.LocalDateTime.parse;

class UserStatsFactory {

    static UserStatsDto userStats(String login, int followers, int publicRepos) {
        return new UserStatsDto(583231, login, "The Octocat", "User",
                "https://avatars.githubusercontent.com/u/583231?v=4", parse("2011-01-25T18:44:36", DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")),
                followers != 0 ? 6.0 / followers * (2 + publicRepos) : 0);
    }
}

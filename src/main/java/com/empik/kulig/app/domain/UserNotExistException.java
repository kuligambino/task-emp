package com.empik.kulig.app.domain;

public class UserNotExistException extends RuntimeException {
    public UserNotExistException(String login) {
        super(String.format("User %s doesn't exist!", login));
    }
}

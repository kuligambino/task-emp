package com.empik.kulig.app.domain;

class UserNotExistException extends RuntimeException {
    UserNotExistException(String login) {
        super(String.format("User %s doesn't exist!", login));
    }
}

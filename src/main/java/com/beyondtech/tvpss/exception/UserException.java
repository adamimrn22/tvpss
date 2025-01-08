package com.beyondtech.tvpss.exception;

public class UserException extends RuntimeException {

    public UserException(String message) {
        super(message);
    }

    public static UserException alreadyExists() {
        return new UserException("User with this email already exists");
    }

    public static UserException fieldNull(String fieldName) {
        return new UserException(fieldName + " cannot be null or empty");
    }

    public static UserException roleNotFound(String roleName) {
        return new UserException("Role " + roleName + " does not exist");
    }

    public static UserException userNotExists(Long id) {
        return new UserException("User with " + id.toString() + " does not exists");
    }
}

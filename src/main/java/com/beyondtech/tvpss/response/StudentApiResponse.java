package com.beyondtech.tvpss.response;

import com.beyondtech.tvpss.model.Student;

import java.util.List;

public class StudentApiResponse {
    private boolean success;
    private String message;
    private List<Student> data;

    // Getters and setters
    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Student> getData() {
        return data;
    }

    public void setData(List<Student> data) {
        this.data = data;
    }
}


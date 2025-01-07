package com.beyondtech.tvpss.response;

import com.beyondtech.tvpss.model.School;

import java.util.List;

public class SchoolApiResponse {

    private boolean success;
    private String message;
    private List<School> data;

    public SchoolApiResponse() {}

    public SchoolApiResponse(boolean success, String message, List<School> data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }

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

    public List<School> getData() {
        return data;
    }

    public void setData(List<School> data) {
        this.data = data;
    }
}

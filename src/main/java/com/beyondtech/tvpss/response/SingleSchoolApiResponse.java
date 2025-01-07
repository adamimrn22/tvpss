package com.beyondtech.tvpss.response;

import com.beyondtech.tvpss.model.School;

public class SingleSchoolApiResponse {
    private boolean success;
    private String message;
    private School data;

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

    public School getData() {
        return data;
    }

    public void setData(School data) {
        this.data = data;
    }
}

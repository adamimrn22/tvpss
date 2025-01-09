package com.beyondtech.tvpss.model;

public enum TvpssStatus {
    PENDING_VALIDASI("PENDING_VALIDASI"),
    DIVALIDASI("DIVALIDASI");

    private final String status;

    TvpssStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}


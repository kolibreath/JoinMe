package com.joinme.model;

/**
 * Created by kolibreath on 17-10-21.
 */

public class AccepterInfoResponse {

    /**
     * status : 1
     * time : fdfd
     */

    private int status;
    private String time;

    public AccepterInfoResponse(int status, String time) {
        this.status = status;
        this.time = time;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}

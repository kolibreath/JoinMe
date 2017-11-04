package com.joinme.model;

/**
 * Created by kolibreath on 17-10-21.
 */

public class RaiserInfo {

    /**
     * raiser_id : fdfd
     * time : fdf
     */

    private String raiser_id;
    private String time;

    public RaiserInfo(String raiser_id, String time) {
        this.raiser_id = raiser_id;
        this.time = time;
    }

    public RaiserInfo(String raiser_id) {
        this.raiser_id = raiser_id;
    }

    public String getRaiser_id() {
        return raiser_id;
    }

    public void setRaiser_id(String raiser_id) {
        this.raiser_id = raiser_id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}

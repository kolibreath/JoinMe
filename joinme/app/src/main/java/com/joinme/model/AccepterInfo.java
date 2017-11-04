package com.joinme.model;

/**
 * Created by kolibreath on 17-10-21.
 */

public class AccepterInfo {

    /**
     * raiser_id : fdfdfd
     * acceptter_id : fd
     */

    private String raiser_id;
    private String acceptter_id;

    public AccepterInfo(String raiser_id) {
        this.raiser_id = raiser_id;
    }

    public AccepterInfo(String raiser_id, String acceptter_id) {
        this.raiser_id = raiser_id;
        this.acceptter_id = acceptter_id;
    }

    public String getRaiser_id() {
        return raiser_id;
    }

    public void setRaiser_id(String raiser_id) {
        this.raiser_id = raiser_id;
    }

    public String getAcceptter_id() {
        return acceptter_id;
    }

    public void setAcceptter_id(String acceptter_id) {
        this.acceptter_id = acceptter_id;
    }
}

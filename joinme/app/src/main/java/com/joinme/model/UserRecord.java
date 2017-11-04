package com.joinme.model;

import java.util.List;

/**
 * Created by kolibreath on 17-10-21.
 */

public class UserRecord {

    /**
     * record : ["dd","fdf"]
     * socre : 1
     */

    private int socre;
    private List<String> record;

    public int getSocre() {
        return socre;
    }

    public void setSocre(int socre) {
        this.socre = socre;
    }

    public List<String> getRecord() {
        return record;
    }

    public void setRecord(List<String> record) {
        this.record = record;
    }
}

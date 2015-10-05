package com.example.tekvy.gcmclient;

/**
 * Created by tekvy on 6/5/15.
 */
public class MemberListModel {

    String email;
    int id;
    String name;
    String gcm_regid;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGcm_regid() {
        return gcm_regid;
    }

    public void setGcm_regid(String gcm_regid) {
        this.gcm_regid = gcm_regid;
    }
}

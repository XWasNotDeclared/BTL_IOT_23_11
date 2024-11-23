package com.example.iottest10_28_19_11.model;

public class DeviceFollowDetail {
    private String username;
    private String phone;
    private String status;

    public DeviceFollowDetail(String username, String phone, String status) {
        this.username = username;
        this.phone = phone;
        this.status = status;
    }

    public String getUsername() {
        return username;
    }

    public String getPhone() {
        return phone;
    }

    public String getStatus() {
        return status;
    }
}

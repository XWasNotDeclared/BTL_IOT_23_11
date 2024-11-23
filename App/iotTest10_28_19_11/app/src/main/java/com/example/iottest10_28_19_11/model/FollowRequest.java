package com.example.iottest10_28_19_11.model;

public class FollowRequest {
    private int deviceId;

    public FollowRequest(int deviceId) {
        this.deviceId = deviceId;
    }

    public int getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(int deviceId) {
        this.deviceId = deviceId;
    }
}

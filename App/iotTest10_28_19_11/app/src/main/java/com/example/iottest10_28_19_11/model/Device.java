package com.example.iottest10_28_19_11.model;

public class Device {
    private int deviceId;
    private String deviceName;
    private int userId;
    private String userName;

    // Constructor
    public Device(int deviceId, String deviceName, int userId, String userName) {
        this.deviceId = deviceId;
        this.deviceName = deviceName;
        this.userId = userId;
        this.userName = userName;
    }

    public Device(String deviceName) {
        this.deviceName = deviceName;
    }

    // Getters
    public int getDeviceId() {
        return deviceId;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public int getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }
}

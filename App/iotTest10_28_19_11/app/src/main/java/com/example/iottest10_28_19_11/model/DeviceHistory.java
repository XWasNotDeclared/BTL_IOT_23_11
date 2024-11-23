package com.example.iottest10_28_19_11.model;
public class DeviceHistory {
    private int id;
    private String date;
    private String longitude;
    private String latitude;
    private String moreInfo;
    private int deviceId;
    private String deviceName;
    private int userId;
    private String deviceOwnerUsername;

    public DeviceHistory(int id, String date, String longitude, String latitude, String moreInfo, int deviceId, String deviceName, int userId, String deviceOwnerUsername) {
        this.id = id;
        this.date = date;
        this.longitude = longitude;
        this.latitude = latitude;
        this.moreInfo = moreInfo;
        this.deviceId = deviceId;
        this.deviceName = deviceName;
        this.userId = userId;
        this.deviceOwnerUsername = deviceOwnerUsername;
    }

    public int getId() {
        return id;
    }

    public String getDate() {
        return date;
    }

    public String getLongitude() {
        return longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getMoreInfo() {
        return moreInfo;
    }

    public int getDeviceId() {
        return deviceId;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public int getUserId() {
        return userId;
    }

    public String getDeviceOwnerUsername() {
        return deviceOwnerUsername;
    }
}

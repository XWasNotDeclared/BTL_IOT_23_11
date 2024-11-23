package com.example.iottest10_28_19_11.model;
public class User {
    private String username;
    private String password;
    private String phoneNumber;
    private String FCM;
    private String name;
    private int age;

    public User(String username, String password, String phoneNumber, String FCM, String name, int age) {
        this.username = username;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.FCM = FCM;
        this.name = name;
        this.age = age;
    }
    public User(String username, String password, String FCM) {
        this.username = username;
        this.password = password;
        this.FCM = FCM;
    }
    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }


    // Getters and setters
}

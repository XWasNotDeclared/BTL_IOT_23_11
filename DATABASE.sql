CREATE DATABASE iotv01;
USE iotv01;

CREATE TABLE user (
    uid INT AUTO_INCREMENT PRIMARY KEY UNIQUE,
    username VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    phoneNumber varchar(255),
    FCM varchar(255),
    name varchar(255),
    age int
);
CREATE TABLE device (
    id INT AUTO_INCREMENT PRIMARY KEY unique,
    deviceName VARCHAR(255) NOT NULL,
    userUid int,
    foreign key (userUid) references user(uid)
);
CREATE TABLE follow (
    id INT AUTO_INCREMENT PRIMARY KEY unique,
    status VARCHAR(255),
	userUid int,
	deviceId int,
    foreign key (userUid) references user(uid),
    foreign key (deviceId) references device(id)
);
CREATE TABLE device_history (
    id INT AUTO_INCREMENT PRIMARY KEY unique,
    date VARCHAR(255) NOT NULL,
    longitude VARCHAR(255) NOT NULL,
    latitude VARCHAR(255) NOT NULL,
    moreInfor VARCHAR(255) NOT NULL,
    deviceId int,
    foreign key (deviceId) references device(id)
);


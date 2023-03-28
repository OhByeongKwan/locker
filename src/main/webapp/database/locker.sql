CREATE DATABASE IF NOT EXISTS locker
DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE locker;

CREATE TABLE IF NOT EXISTS uni(
    ID int NOT NULL AUTO_INCREMENT PRIMARY KEY ,
    uniName VARCHAR(128),
    status VARCHAR(128),
    jsonstr VARCHAR(8192),
    INDEX idx1(uniName)
    );
-- status 1 = exist , 2 = request

CREATE TABLE IF NOT EXISTS addDepartment(
    ID int NOT NULL AUTO_INCREMENT PRIMARY KEY ,
    uid int(128),
    department VARCHAR(512),
    email VARCHAR(512),
    INDEX idx1(uid)
    );
-- status 1 = exist , 2 = request



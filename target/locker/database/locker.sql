CREATE DATABASE IF NOT EXISTS locker
DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE locker;

-- status 1 = exist , 2 = request
CREATE TABLE IF NOT EXISTS uni(
    ID int NOT NULL AUTO_INCREMENT PRIMARY KEY ,
    uniName VARCHAR(128),
    status VARCHAR(128),
    jsonstr VARCHAR(8192),
    INDEX idx1(uniName)
    );

CREATE TABLE IF NOT EXISTS department(
    ID int NOT NULL AUTO_INCREMENT PRIMARY KEY ,
    uid int(128),
    uniName VARCHAR(128),
    depName VARCHAR(128),
    INDEX idx1(ID),
    INDEX idx2(uid)
    );

CREATE TABLE IF NOT EXISTS addDepartment(
    ID int NOT NULL AUTO_INCREMENT PRIMARY KEY ,
    uid int(128),
    department VARCHAR(512),
    email VARCHAR(512),
    INDEX idx1(uid)
    );
-- status 1 = exist , 2 = request

CREATE TABLE IF NOT EXISTS user (
    mid VARCHAR(128) PRIMARY KEY,
    password VARCHAR(32),
    depCode VARCHAR(32),
    type CHAR(4) DEFAULT 'U',
    authcode VARCHAR(32),
    jsonstr VARCHAR(1024),
    ts TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS lockerForm (
    id int NOT NULL AUTO_INCREMENT PRIMARY KEY ,
    status VARCHAR(128) lDEFAULT 'N',
    depCode int(128),
    jsonstr VARCHAR(1024),
    ts TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- CREATE TABLE if not exists lock<id> (
--     numCode VARCHAR(4), - A .. b.. c
--     num VARCHAR(32),     - 1  .. 2 .. 3
--     status VARCHAR(128) DEFAULT 'N',
--     mid VARCHAR(128),
--     password VARCHAR(32),
-- )
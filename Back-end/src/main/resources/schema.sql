-- MySQL Workbench Forward Engineering
-- -----------------------------------------------------
-- Schema pharmacyapp
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS pharmacyapp;
USE pharmacyapp;

-- -----------------------------------------------------
-- Table Users
-- -----------------------------------------------------

DROP TABLE IF EXISTS Users;

CREATE TABLE Users
(
    id          INT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    first_name  VARCHAR(45) NOT NULL,
    middle_name VARCHAR(45) NULL,
    last_name   VARCHAR(45) NOT NULL,
    email       VARCHAR(45) NOT NULL,
    password    VARCHAR(255) DEFAULT NULL,
    enabled BOOLEAN DEFAULT FALSE,
    using_mfa BOOLEAN DEFAULT FALSE,
    image_url VARCHAR(255) DEFAULT 'https://cdn.pixabay.com/photo/2015/10/05/22/37/blank-profile-picture-973460_1280.png',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    phone VARCHAR(45) DEFAULT NULL,
    CONSTRAINT UQ_Users_Email UNIQUE (email)
);

-- -----------------------------------------------------
-- Table Roles
-- -----------------------------------------------------

DROP TABLE IF EXISTS Roles;

CREATE TABLE Roles
(
    id             INT UNSIGNED NOT NULL  PRIMARY KEY ,
    name           VARCHAR(45) NOT NULL,
    permissions    VARCHAR(255) NOT NULL,
    CONSTRAINT UQ_Roles_Name UNIQUE (name)
);

-- -----------------------------------------------------
-- Table UserRoles
-- -----------------------------------------------------

DROP TABLE IF EXISTS UserRoles;

CREATE TABLE UserRoles
(
    id INT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    user_id INT UNSIGNED NOT NULL,
    role_id INT UNSIGNED NOT NULL,
    FOREIGN KEY (user_id) REFERENCES Users (id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (role_id) REFERENCES Roles (id) ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT UQ_UserRole_User_Id UNIQUE (user_id)
);

-- -----------------------------------------------------
-- Table AccountVerification
-- -----------------------------------------------------

DROP TABLE IF EXISTS AccountVerifications;

CREATE TABLE AccountVerifications
(
    id        INT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    url       VARCHAR(45) NOT NULL,
    user_id   INT UNSIGNED NOT NULL,
    FOREIGN KEY (user_id) REFERENCES Users (id) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT UQ_AccountVerifications_User_Id UNIQUE (user_id),
    CONSTRAINT UQ_AccountVerifications_Url UNIQUE (url)
);

-- -----------------------------------------------------
-- Table TwoFactorVerifications
-- -----------------------------------------------------

DROP TABLE IF EXISTS TwoFactorVerifications;

CREATE TABLE TwoFactorVerifications
(
   id INT UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
   code VARCHAR(45) NOT NULL,
   user_id INT UNSIGNED NOT NULL,
   expiration_date DATETIME NOT NULL,
   FOREIGN KEY (user_id) REFERENCES Users (id) ON DELETE CASCADE ON UPDATE CASCADE,
   CONSTRAINT UQ_TwoFactorVerifications_User_Id UNIQUE (user_id),
   CONSTRAINT UQ_TwoFactorVerifications_Code UNIQUE (code)
);

-- -----------------------------------------------------
-- Table Events
-- -----------------------------------------------------

DROP TABLE IF EXISTS Events;

CREATE TABLE Events
(
   id INT UNSIGNED NOT NULL  AUTO_INCREMENT PRIMARY KEY,
   type VARCHAR(100) NOT NULL CHECK(type IN ('LOGIN_ATTEMPT', 'LOGIN_ATTEMPT_FAILED', 'LOGIN_ATTEMPT_SUCCESS',
   'PROFILE_UPDATE','PROFILE_PICTURE_UPDATE','ROLE_UPDATE', 'ACCOUNT_SETTINGS_UPDATE', 'MFA_UPDATE')),
   description VARCHAR(255) NULL,
   CONSTRAINT UQ_Events_Type UNIQUE (type)
);

-- -----------------------------------------------------
-- Table UserEvents
-- -----------------------------------------------------

DROP TABLE IF EXISTS UserEvents;

CREATE TABLE UserEvents
(
    id         INT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    user_id    INT UNSIGNED NOT NULL,
    event_id   INT UNSIGNED NOT NULL,
    device     VARCHAR(100) DEFAULT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES Users (id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (event_id) REFERENCES Events (id) ON DELETE RESTRICT ON UPDATE CASCADE
);

-- -----------------------------------------------------
-- Table ResetPasswordVerifications
-- -----------------------------------------------------

DROP TABLE IF EXISTS ResetPasswordVerifications;

CREATE TABLE ResetPasswordVerifications
(
    id         INT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    url        VARCHAR(255) DEFAULT NULL,
    expiration_date DATETIME NOT NULL,
    user_id  INT UNSIGNED NOT NULL,
    FOREIGN KEY (user_id) REFERENCES Users (id) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT UQ_ResetPasswordVerifications_User_Id UNIQUE (user_id),
    CONSTRAINT UQ_ResetPasswordVerifications_Url UNIQUE (url)
);

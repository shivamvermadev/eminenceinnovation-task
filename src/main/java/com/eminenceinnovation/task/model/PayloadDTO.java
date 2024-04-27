package com.eminenceinnovation.task.model;

public class PayloadDTO {
    String userName;

    String issuedAt;

    String expirationTime;

    public PayloadDTO() {
    }

    public PayloadDTO(String userName, String issuedAt, String expirationTime) {
        this.userName = userName;
        this.issuedAt = issuedAt;
        this.expirationTime = expirationTime;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getIssuedAt() {
        return issuedAt;
    }

    public void setIssuedAt(String issuedAt) {
        this.issuedAt = issuedAt;
    }

    public String getExpirationTime() {
        return expirationTime;
    }

    public void setExpirationTime(String expirationTime) {
        this.expirationTime = expirationTime;
    }
}

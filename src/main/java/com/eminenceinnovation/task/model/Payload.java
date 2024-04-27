package com.eminenceinnovation.task.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Payload {

    @JsonProperty("sub")
    String userName;

    @JsonProperty("iat")
    Integer issuedAt;

    @JsonProperty("exp")
    Integer expirationTime;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getIssuedAt() {
        return issuedAt;
    }

    public void setIssuedAt(Integer issuedAt) {
        this.issuedAt = issuedAt;
    }

    public Integer getExpirationTime() {
        return expirationTime;
    }

    public void setExpirationTime(Integer expirationTime) {
        this.expirationTime = expirationTime;
    }

    @Override
    public String toString() {
        return "{ username: " + this.userName + ", issuedAt: " + this.issuedAt + ", expirationTime: " + this.expirationTime + "}";
    }
}
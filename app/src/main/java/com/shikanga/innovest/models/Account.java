package com.shikanga.innovest.models;

import com.google.gson.annotations.SerializedName;

public class Account {
    @SerializedName("user")
    private String user;
    @SerializedName("picture")
    private String picture;
    @SerializedName("interests")
    private String[] interests;
    @SerializedName("balance")
    private float balance;
    @SerializedName("telephone")
    private String telephone;

    public Account(String user, String picture, String[] interests, float balance, String telephone) {
        this.user = user;
        this.picture = picture;
        this.interests = interests;
        this.balance = balance;
        this.telephone = telephone;
    }

    public Account() {
        this.user = null;
        this.picture = null;
        this.interests = null;
        this.balance = 0;
        this.telephone = null;
    }

    @Override
    public String toString() {
        return  this.user;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String[] getInterests() {
        return interests;
    }

    public void setInterests(String[] interests) {
        this.interests = interests;
    }

    public float getBalance() {
        return balance;
    }

    public void setBalance(float balance) {
        this.balance = balance;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }
}

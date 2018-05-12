package com.shikanga.innovest.models;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class Bid {
    @SerializedName("user")
    private String user;
    @SerializedName("amount")
    private float amount;
    @SerializedName("date")
    private Date date;
    @SerializedName("campaign")
    private String campaign;

    public Bid(String user, float amount, Date date, String campaign) {
        this.user = user;
        this.amount = amount;
        this.date = date;
        this.campaign = campaign;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getCampaign() {
        return campaign;
    }

    public void setCampaign(String campaign) {
        this.campaign = campaign;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}

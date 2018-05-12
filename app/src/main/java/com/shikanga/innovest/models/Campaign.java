package com.shikanga.innovest.models;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class Campaign {
    @SerializedName("id")
    private  int id;
    @SerializedName("name")
    private String name;
    @SerializedName("user")
    private int user;
    @SerializedName("summary")
    private String summary;
    @SerializedName("description")
    private String description;
    @SerializedName("amount")
    private float amount;
    @SerializedName("picture")
    private String picture;
    @SerializedName("start")
    private Date start;
    @SerializedName("end")
    private Date end;
    @SerializedName("category")
    private int category;

    public Campaign(String name, int user, String summary, String description, float amount, String picture, Date start, Date end, int category) {
        this.name = name;
        this.user = user;
        this.summary = summary;
        this.description = description;
        this.amount = amount;
        this.picture = picture;
        this.start = start;
        this.end = end;
        this.category = category;
    }

    public Campaign(int id, String name, int user, String summary, String description, float amount, String picture, Date start, Date end, int category) {
        this.id = id;
        this.name = name;
        this.user = user;
        this.summary = summary;
        this.description = description;
        this.amount = amount;
        this.picture = picture;
        this.start = start;
        this.end = end;
        this.category = category;
    }

    @Override
    public String toString() {
        return "Campaign{" +
                "name='" + name + '\'' +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getUser() {
        return user;
    }

    public void setUser(int user) {
        this.user = user;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }
}

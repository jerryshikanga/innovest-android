package com.shikanga.innovest.models;

public class Category {
    private int id;
    private String name;
    private String summary;
    private String description;
    private String picture;

    public Category() {
    }

    public Category(int id, String name, String summary, String description, String picture) {
        this.id = id;
        this.name = name;
        this.summary = summary;
        this.description = description;
        this.picture = picture;
    }

    public Category(String name, String summary, String description, String picture) {
        this.name = name;
        this.summary = summary;
        this.description = description;
        this.picture = picture;
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

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }
}

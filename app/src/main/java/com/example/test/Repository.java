package com.example.test;
import com.google.gson.annotations.SerializedName;

public class Repository {
    @SerializedName("name")
    private String name;

    @SerializedName("language")
    private String language;

    @SerializedName("description")
    private String description;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

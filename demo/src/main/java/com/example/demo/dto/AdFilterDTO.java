package com.example.demo.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AdFilterDTO {
    private String customWord;
    private String location;
    private String category;

    public String getCustomWord() {
        return customWord;
    }

    public void setCustomWord(String customWord) {
        this.customWord = customWord;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}

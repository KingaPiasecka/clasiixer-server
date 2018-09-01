package com.example.demo.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.example.demo.domain.Ad;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AdDTO {
    private Ad details;
    private byte[] image;

    public Ad getDetails() {
        return details;
    }

    public void setDetails(Ad details) {
        this.details = details;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }
}

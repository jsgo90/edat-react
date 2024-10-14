package com.edat.web.rest.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class YoloResponse {

    @JsonProperty("distance")
    private double distance;

    @JsonProperty("match")
    private boolean match;

    @JsonProperty("message")
    private String message;

    @JsonProperty("ocr_results")
    private OcrResults ocrResults;

    @JsonProperty("image_base64")
    private String imageBase64;

    // Getters y Setters
    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public boolean isMatch() {
        return match;
    }

    public void setMatch(boolean match) {
        this.match = match;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public OcrResults getOcrResults() {
        return ocrResults;
    }

    public void setOcrResults(OcrResults ocrResults) {
        this.ocrResults = ocrResults;
    }

    public String getImageBase64() {
        return imageBase64;
    }

    public void setImageBase64(String imageBase64) {
        this.imageBase64 = imageBase64;
    }
}

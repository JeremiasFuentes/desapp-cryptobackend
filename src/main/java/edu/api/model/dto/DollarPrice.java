package edu.api.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DollarPrice {
    private String d;
    private Float v;

    public DollarPrice() {
    }

    public DollarPrice(String d, Float v) {
        this.d = d;
        this.v = v;
    }

    public String getD() {
        return d;
    }

    public void setD(String d) {
        this.d = d;
    }

    public Float getV() {
        return v;
    }

    public void setV(Float v) {
        this.v = v;
    }
}

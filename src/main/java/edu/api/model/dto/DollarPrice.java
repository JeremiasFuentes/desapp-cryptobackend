package edu.api.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

public class DollarPrice {
    @JsonProperty("fecha")
    private String date;
    @JsonProperty("compra")
    private Float buy;
    @JsonProperty("venta")
    private Float sell;

    public DollarPrice() {
    }

    public DollarPrice(String date, Float buy, Float sell) {
        this.date = date;
        this.buy = buy;
        this.sell = sell;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Float getBuy() {
        return buy;
    }

    public void setBuy(Float buy) {
        this.buy = buy;
    }

    public Float getSell() {
        return sell;
    }

    public void setSell(Float sell) {
        this.sell = sell;
    }
}

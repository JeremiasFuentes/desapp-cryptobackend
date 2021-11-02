package edu.api.model.dto;

import java.io.Serializable;

public class Crypto implements Serializable {
    String symbol;
    Float price;
    String lastUpdate;

    public Crypto() {
    }

    public Crypto(String symbol, float v) {
        this.symbol = symbol;
        this.price = v;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public String getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(String lastUpdate) {
        this.lastUpdate = lastUpdate;
    }
}

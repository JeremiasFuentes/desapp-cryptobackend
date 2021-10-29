
package edu.api.model.entity;


import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
public class Publication {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private LocalDateTime date;
    @NotNull
    private String userNameSeller;
    @NotNull
    private String cryptoName;
    @NotNull
    private float amountOfCrypto;
    @NotNull
    private float priceOfCrypto;
    @NotNull
    private float priceTotalInPesos;
    @NotNull
    private float reputation = 0;
    @NotNull
    private String type;

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public void setReputation(Float reputation) {
        this.reputation = reputation;
    }

    public String getCryptoName() {
        return cryptoName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setCryptoName(String cryptoName) {
        this.cryptoName = cryptoName;
    }

    public Publication() {
    }

    public Publication(LocalDateTime date, String userNameSeller, String cryptoName, float amountOfCrypto, float priceOfCrypto, float priceTotalInPesos, String type, Float reputation) {
        this.date = date;
        this.userNameSeller = userNameSeller;
        this.cryptoName = cryptoName;
        this.amountOfCrypto = amountOfCrypto;
        this.priceOfCrypto = priceOfCrypto;
        this.priceTotalInPesos = priceTotalInPesos;
        this.reputation = reputation;
        this.type = type;
    }

    public String getUserNameSeller() {
        return userNameSeller;
    }

    public void setUserNameSeller(String userNameSeller) {
        this.userNameSeller = userNameSeller;
    }

    public float getAmountOfCrypto() {
        return amountOfCrypto;
    }

    public void setAmountOfCrypto(float amountOfCrypto) {
        this.amountOfCrypto = amountOfCrypto;
    }

    public float getPriceOfCrypto() {
        return priceOfCrypto;
    }

    public void setPriceOfCrypto(float priceOfCrypto) {
        this.priceOfCrypto = priceOfCrypto;
    }

    public float getPriceTotalInPesos() {
        return priceTotalInPesos;
    }

    public void setPriceTotalInPesos(float priceTotalInPesos) {
        this.priceTotalInPesos = priceTotalInPesos;
    }

    public float getReputation() {
        return reputation;
    }

    public void setReputation(float reputation) {
        this.reputation = reputation;
    }
}


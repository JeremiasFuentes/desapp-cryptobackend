
package edu.api.model.entity;


import edu.api.model.dto.Crypto;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private LocalDateTime date;
    @NotNull
    private String userNameSeller;
    @NotBlank
    private String userNameBuyer;
    @NotNull
    private String cryptoName;
    @NotNull
    private float amountOfCrypto;
    @NotNull
    private float priceOfCrypto;
    @NotNull
    private float priceTotalInPesos;
    @NotNull
    private float amountOfCryptoToBuy;
    @NotNull
    private String type;
    private boolean closed = false;
    @ManyToOne
    private Publication publication;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isClosed() {
        return closed;
    }

    public void setClosed(boolean closed) {
        this.closed = closed;
    }

    public String getUserNameBuyer() {
        return userNameBuyer;
    }

    public void setUserNameBuyer(String userNameBuyer) {
        this.userNameBuyer = userNameBuyer;
    }

    public String getCryptoName() {
        return cryptoName;
    }

    public void setCryptoName(String cryptoName) {
        this.cryptoName = cryptoName;
    }

    public Transaction() {
    }

    public Publication getPublication() {
        return publication;
    }

    public void setPublication(Publication publication) {
        this.publication = publication;
    }

    public Transaction(LocalDateTime date, String userNameSeller, String userNameBuyer, String cryptoName, float amountOfCrypto, float priceOfCrypto, float priceTotalInPesos, float amountOfCryptoToBuy, String type , Publication publication) {
        this.date = date;
        this.type = type;
        this.userNameBuyer = userNameBuyer;
        this.userNameSeller = userNameSeller;
        this.cryptoName = cryptoName;
        this.amountOfCrypto = amountOfCrypto;
        this.priceOfCrypto = priceOfCrypto;
        this.priceTotalInPesos = priceTotalInPesos;
        this.amountOfCryptoToBuy = amountOfCryptoToBuy;
        this.publication = publication;
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

    public float getAmountOfCryptoToBuy() {
        return amountOfCryptoToBuy;
    }

    public void setAmountOfCryptoToBuy(float amountOfCryptoToBuy) {
        this.amountOfCryptoToBuy = amountOfCryptoToBuy;
    }
}



package edu.api.model.entity;


import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private LocalDateTime date;
    @ManyToOne
    @JoinColumn(name="userPublisher")
    private User userPublisher;
    @ManyToOne
    @JoinColumn(name="userClient")
    private User userClient;
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
    private boolean transfer = false;
    private boolean confirm = false;

    public boolean isTransfer() {
        return transfer;
    }

    public void setTransfer(boolean transfer) {
        this.transfer = transfer;
    }

    public boolean isConfirm() {
        return confirm;
    }

    public void setConfirm(boolean confirm) {
        this.confirm = confirm;
    }

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

    public Transaction(LocalDateTime date, User userPublisher, User userClient, String cryptoName, float amountOfCrypto, float priceOfCrypto, float priceTotalInPesos, float amountOfCryptoToBuy, String type , Publication publication) {
        this.date = date;
        this.type = type;
        this.userPublisher = userPublisher;
        this.userClient = userClient;
        this.cryptoName = cryptoName;
        this.amountOfCrypto = amountOfCrypto;
        this.priceOfCrypto = priceOfCrypto;
        this.priceTotalInPesos = priceTotalInPesos;
        this.amountOfCryptoToBuy = amountOfCryptoToBuy;
        this.publication = publication;
    }

    @JsonBackReference
    public User getUserPublisher() {
        return userPublisher;
    }

    public void setUserPublisher(User userPublisher) {
        this.userPublisher = userPublisher;
    }

    @JsonBackReference
    public User getUserClient() {
        return userClient;
    }

    public void setUserClient(User userClient) {
        this.userClient = userClient;
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


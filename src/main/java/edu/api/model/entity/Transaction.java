/*

package edu.api.model.entity;


import edu.api.model.dto.Crypto;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NotNull
    private String userNameBuyer;
    @NotNull
    private String userNameSeller;
    @NotNull
    private Crypto crypto;
    @NotNull
    private float amountOfCrypto;
    @NotNull
    private float amountInPesos;

    public Transaction(String userNameBuyer, String userNameSeller, Crypto crypto, float amountOfCrypto, float amountInPesos) {
        this.userNameBuyer = userNameBuyer;
        this.userNameSeller = userNameSeller;
        this.crypto = crypto;
        this.amountOfCrypto = amountOfCrypto;
        this.amountInPesos = amountInPesos;
    }

    public String getUserNameBuyer() {
        return userNameBuyer;
    }

    public void setUserNameBuyer(String userNameBuyer) {
        this.userNameBuyer = userNameBuyer;
    }

    public String getUserNameSeller() {
        return userNameSeller;
    }

    public void setUserNameSeller(String userNameSeller) {
        this.userNameSeller = userNameSeller;
    }

    public Crypto getCrypto() {
        return crypto;
    }

    public void setCrypto(Crypto crypto) {
        this.crypto = crypto;
    }

    public float getAmountOfCrypto() {
        return amountOfCrypto;
    }

    public void setAmountOfCrypto(float amountOfCrypto) {
        this.amountOfCrypto = amountOfCrypto;
    }

    public float getAmountInPesos() {
        return amountInPesos;
    }

    public void setAmountInPesos(float amountInPesos) {
        this.amountInPesos = amountInPesos;
    }
}

*/
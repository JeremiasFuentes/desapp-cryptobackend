package edu.api.model.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class NewTransaction {



    @NotBlank
    private String userNamePublisher;
    @NotBlank
    private String userNameClient;
    @NotBlank
    private String cryptoName;
    @NotNull
    private float amountOfCrypto;
    @NotNull
    private float priceTotalInPesos;
    @NotBlank
    private String type;

    public String getUserNameClient() {
        return userNameClient;
    }

    public void setUserNameClient(String userNameClient) {
        this.userNameClient = userNameClient;
    }



    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUserNamePublisher() {
        return userNamePublisher;
    }

    public void setUserNamePublisher(String userNamePublisher) {
        this.userNamePublisher = userNamePublisher;
    }

    public String getCryptoName() {
        return cryptoName;
    }

    public void setCryptoName(String cryptoName) {
        this.cryptoName = cryptoName;
    }

    public float getAmountOfCrypto() {
        return amountOfCrypto;
    }

    public void setAmountOfCrypto(float amountOfCrypto) {
        this.amountOfCrypto = amountOfCrypto;
    }

    public float getPriceTotalInPesos() {
        return priceTotalInPesos;
    }

    public void setPriceTotalInPesos(float priceTotalInPesos) {
        this.priceTotalInPesos = priceTotalInPesos;
    }


}

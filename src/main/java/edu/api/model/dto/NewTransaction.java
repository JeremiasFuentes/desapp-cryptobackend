package edu.api.model.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class NewTransaction {



    @NotBlank
    private String userNameSeller;
    @NotBlank
    private String userNameBuyer;
    @NotBlank
    private String cryptoName;
    @NotNull
    private float amountOfCrypto;
    @NotNull
    private float priceTotalInPesos;
    @NotBlank
    private String type;

    public String getUserNameBuyer() {
        return userNameBuyer;
    }

    public void setUserNameBuyer(String userNameBuyer) {
        this.userNameBuyer = userNameBuyer;
    }



    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUserNameSeller() {
        return userNameSeller;
    }

    public void setUserNameSeller(String userNameSeller) {
        this.userNameSeller = userNameSeller;
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

package edu.api.model.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

public class RegisterUser {
    @NotBlank
    @Size(min = 10, max = 30, message
            = "Name must be between 10 and 30 characters")
    private String name;
    @NotBlank
    @Size(min = 10, max = 30, message
            = "LastName must be between 10 and 30 characters")
    private String lastName;
    @NotBlank
    @Size(min = 10, max = 30, message
            = "userName must be between 10 and 30 characters")
    private String userName;
    @Email
    private String email;
    @NotBlank
    private String password;
    @Size(min = 0, max = 30, message
            = "Direction must be between 0 and 30 characters")
    private String direction;
    @NotBlank
    @Size(min = 22, max = 22, message
                = "CVU must be 22 Digits")
    private String cvu;
    @NotBlank
    @Size(min = 8, max = 8, message
            = "Wallet must be 8 digits")
    private String wallet;
    private Set<String> rols = new HashSet<>();

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getCvu() {
        return cvu;
    }

    public void setCvu(String cvu) {
        this.cvu = cvu;
    }

    public String getWallet() {
        return wallet;
    }

    public void setWallet(String wallet) {
        this.wallet = wallet;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<String> getRols() {
        return rols;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setRols(Set<String> roles) {
        this.rols = rols;
    }
}

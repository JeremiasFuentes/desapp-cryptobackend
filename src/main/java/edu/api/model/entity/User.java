package edu.api.model.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NotNull
    private String name;
    @NotNull
    private String lastName;
    @NotNull
    @Column(unique = true)
    private String userName;
    @NotNull
    private String email;
    @NotNull
    private String password;
    @NotNull
    private String direction;
    @NotNull
    private String cvu;
    @Column(unique = true)
    @NotNull
    private String wallet;
    @NotNull
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_rol", joinColumns = @JoinColumn(name = "user_id"),
    inverseJoinColumns = @JoinColumn(name = "rol_id"))
    private Set<Rol> rols = new HashSet<>();

    @OneToMany(mappedBy = "userPublisher", cascade =CascadeType.ALL, fetch = FetchType.LAZY, targetEntity = Transaction.class)
    private List<Transaction> transactionsPublisher = new ArrayList<Transaction>();
    @OneToMany(mappedBy = "userClient", cascade =CascadeType.ALL, fetch = FetchType.LAZY, targetEntity = Transaction.class)
    private List<Transaction> transactionsClient = new ArrayList<Transaction>();

    @OneToMany(mappedBy = "user", cascade =CascadeType.ALL, fetch = FetchType.LAZY, targetEntity = Publication.class)
    private List<Publication> publications = new ArrayList<Publication>();
    private float reputation = 0;
    private int cantTrxFinished = 0;
    private int points = 0;

    public float getReputation() {
        return this.reputation;
    }

    public void setReputation(float reputation) {
        this.reputation = reputation;
    }

    public int getCantTrxFinished() {
        return cantTrxFinished;
    }

    public void setCantTrxFinished(int cantTrxFinished) {
        this.cantTrxFinished = cantTrxFinished;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    @JsonManagedReference
    public List<Transaction> getTransactionsPublisher() {
        return transactionsPublisher;
    }

    public void setTransactionsPublisher(List<Transaction> transactions) {
        this.transactionsPublisher = transactions;
    }

    @JsonManagedReference
    public List<Transaction> getTransactionsClient() {
        return transactionsClient;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactionsClient = transactions;
    }

    public User() {
    }

    public User(@NotNull String name,@NotNull String lastName,@NotNull String userName,@NotNull String email,@NotNull String password,@NotNull String direction,@NotNull String cvu,@NotNull String wallet) {
        this.name = name;
        this.lastName = lastName;
        this.userName = userName;
        this.email = email;
        this.password = password;
        this.direction = direction;
        this.cvu = cvu;
        this.wallet = wallet;
    }

    @JsonManagedReference
    public List<Publication> getPublications() {
        return publications;
    }

    public void setPublications(List<Publication> publications) {
        this.publications = publications;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public Set<Rol> getRols() {
        return rols;
    }

    public void setRols(Set<Rol> roles) {
        this.rols = roles;
    }

    public void recalculateReputation() {
        if(this.cantTrxFinished == 0){
            this.reputation = 0;
        }else{
            this.reputation = this.points / this.cantTrxFinished;
        }
    }
}

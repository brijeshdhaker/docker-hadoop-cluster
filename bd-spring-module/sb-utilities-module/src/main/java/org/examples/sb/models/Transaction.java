package org.examples.sb.models;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Random;
import java.util.UUID;

@Getter
@Setter
public class Transaction implements Serializable {

    private Integer id;
    private String uuid;
    private String cardtype;
    private String website;
    private String product;
    private Double amount;
    private String city;
    private String country;
    private Long addts;

    public static Transaction builder(){
        Random random = new Random();
        Transaction t = new Transaction();
        t.uuid = UUID.randomUUID().toString();
        t.addts = System.currentTimeMillis();
        return t;
    }

    public Transaction id(Integer id){
        this.id = id;
        return this;
    }

    public Transaction cardtype(String cardtype){
        this.cardtype = cardtype;
        return this;
    }

    public Transaction website(String website){
        this.website = website;
        return this;
    }

    public Transaction product(String product){
        this.product = product;
        return this;
    }

    public Transaction amount(Double amount){
        this.amount = amount;
        return this;
    }

    public Transaction city(String city){
        this.city = city;
        return this;
    }

    public Transaction country(String country){
        this.country = country;
        return this;
    }
}

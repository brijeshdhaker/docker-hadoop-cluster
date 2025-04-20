package org.examples.sb.models;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Random;
import java.util.UUID;

@Getter
@Setter
public class Transaction implements Serializable {

    private java.lang.Integer id;
    private java.lang.String uuid;
    private java.lang.String cardtype;
    private java.lang.String website;
    private java.lang.String product;
    private java.lang.Double amount;
    private java.lang.String city;
    private java.lang.String country;
    private java.lang.Long addts;

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

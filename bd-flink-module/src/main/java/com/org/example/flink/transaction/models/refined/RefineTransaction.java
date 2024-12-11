package com.org.example.flink.transaction.models.refined;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import java.util.Random;
import java.util.UUID;

@Getter
@Setter
public class RefineTransaction implements Comparable<RefineTransaction>, Serializable {

    private java.lang.Long id;
    private java.lang.String uuid;
    private java.lang.String cardtype;
    private java.lang.String website;
    private java.lang.String product;
    private java.lang.Double amount;
    private java.lang.String city;
    private java.lang.String country;
    private java.lang.Long addts;
    private java.time.Instant eventTime;

    public static RefineTransaction builder(){
        Random random = new Random();
        RefineTransaction t = new RefineTransaction();
        t.uuid = UUID.randomUUID().toString();
        t.addts = System.currentTimeMillis();
        t.eventTime = Instant.now();
        return t;
    }

    public RefineTransaction id(Long id){
        this.id = id;
        return this;
    }

    public RefineTransaction cardtype(String cardtype){
        this.cardtype = cardtype;
        return this;
    }

    public RefineTransaction website(String website){
        this.website = website;
        return this;
    }

    public RefineTransaction product(String product){
        this.product = product;
        return this;
    }

    public RefineTransaction amount(Double amount){
        this.amount = amount;
        return this;
    }

    public RefineTransaction city(String city){
        this.city = city;
        return this;
    }

    public RefineTransaction country(String country){
        this.country = country;
        return this;
    }

    @Override
    public int compareTo(RefineTransaction transaction) {
        return 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash();
    }

    @Override
    public String toString() {
        return id + ", "
                + uuid + ", "
                + cardtype + ", "
                + website + ", "
                + product + ", "
                + amount + ", "
                + city + ", "
                + country + ", "
                + addts;
    }
}

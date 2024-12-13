package com.org.example.flink.transaction.models.raw;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import java.util.Random;
import java.util.UUID;

@Getter
@Setter
public class RawTransaction implements Comparable<RawTransaction>, Serializable {

    private Long id;
    private String uuid;
    private String cardType;
    private String website;
    private String product;
    private Float amount;
    private String city;
    private String country;
    private Long eventTime;

    public static RawTransaction builder(){
        Random random = new Random();
        RawTransaction t = new RawTransaction();
        t.uuid = UUID.randomUUID().toString();
        t.eventTime = Instant.now().getEpochSecond();
        return t;
    }

    public RawTransaction id(Long id){
        this.id = id;
        return this;
    }

    public RawTransaction cardtype(String cardType){
        this.cardType = cardType;
        return this;
    }

    public RawTransaction website(String website){
        this.website = website;
        return this;
    }

    public RawTransaction product(String product){
        this.product = product;
        return this;
    }

    public RawTransaction amount(Float amount){
        this.amount = amount;
        return this;
    }

    public RawTransaction city(String city){
        this.city = city;
        return this;
    }

    public RawTransaction country(String country){
        this.country = country;
        return this;
    }

    @Override
    public int compareTo(RawTransaction transaction) {
        return 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash();
    }

    @Override
    public String toString() {
        return "Raw Transaction : " +id + ", "
                + uuid + ", "
                + cardType + ", "
                + website + ", "
                + product + ", "
                + amount + ", "
                + city + ", "
                + country + ", "
                + eventTime;
    }
}

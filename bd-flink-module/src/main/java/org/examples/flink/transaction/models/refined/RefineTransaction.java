package org.examples.flink.transaction.models.refined;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.beanutils.BeanUtils;

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
    private java.lang.String cardType;
    private java.lang.String website;
    private java.lang.String product;
    private java.lang.Float amount;
    private java.lang.String city;
    private java.lang.String country;
    private java.lang.Long eventTime;
    private java.lang.Long addts;

    public static RefineTransaction builder(){
        RefineTransaction t = new RefineTransaction();
        t.addts = System.currentTimeMillis();
        return t;
    }

    public RefineTransaction id(Long id){
        this.id = id;
        return this;
    }

    public RefineTransaction cardType(String cardType){
        this.cardType = cardType;
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

    public RefineTransaction amount(Float amount){
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
        return "Refined Transaction : " +id + ", "
                + uuid + ", "
                + cardType + ", "
                + website + ", "
                + product + ", "
                + amount + ", "
                + city + ", "
                + country + ", "
                + addts + ", "
                + eventTime;
    }
}

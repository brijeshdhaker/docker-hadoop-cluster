package org.examples.flink.transaction.models.enriched;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
public class EnrichedTransaction implements Comparable<EnrichedTransaction>, Serializable {

    private Long id;
    private String uuid;
    private String cardType;
    private String website;
    private String product;
    private Float amount;
    private String city;
    private String country;
    private Long eventTime;
    private Long addts;

    public static EnrichedTransaction builder(){
        EnrichedTransaction t = new EnrichedTransaction();
        t.addts = System.currentTimeMillis();
        return t;
    }

    public EnrichedTransaction id(Long id){
        this.id = id;
        return this;
    }

    public EnrichedTransaction cardType(String cardType){
        this.cardType = cardType;
        return this;
    }

    public EnrichedTransaction website(String website){
        this.website = website;
        return this;
    }

    public EnrichedTransaction product(String product){
        this.product = product;
        return this;
    }

    public EnrichedTransaction amount(Float amount){
        this.amount = amount;
        return this;
    }

    public EnrichedTransaction city(String city){
        this.city = city;
        return this;
    }

    public EnrichedTransaction country(String country){
        this.country = country;
        return this;
    }

    @Override
    public int compareTo(EnrichedTransaction transaction) {
        return 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash();
    }

    @Override
    public String toString() {
        return "Enriched Transaction : " + id + ", "
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

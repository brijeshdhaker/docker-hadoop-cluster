package org.examples.sb.repositories.entities;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name="PRODUCTS")
public class ProductEntity {

    //@GeneratedValue(strategy= GenerationType.AUTO)
    @Id
    @Column(name="ID")
    private Long productId;

    @Column(name="NAME")
    private String productName;

    @Column(name="PRICE")
    private java.lang.Double price;

    @Column(name="STATUS")
    private String status;

    @Column(name="QUANTITY")
    private String quantity;

    @Convert(converter = LocalDateTimeConverter.class)
    @Column(name = "ADD_TS")
    protected LocalDateTime addTs;

    @Convert(converter = LocalDateTimeConverter.class)
    @Column(name = "UPD_TS")
    protected LocalDateTime updTs;

    @PrePersist
    public void prePersist() {
        addTs = updTs = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        updTs = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return "sample.multimodule.domain.AssetEntity[ id=" + productId + " ]";
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.examples.sb.repositories.entities;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 *
 * @author brijeshdhaker
 */
@Entity
@Table(name= "ROLES")
public class RoleEntity implements Serializable {

    //@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ROLE_SEQ_GENERATOR")
    //@SequenceGenerator(name= "ROLE_SEQ_GENERATOR", sequenceName = "ROLE_SEQUENCE", schema = "PUBLIC", allocationSize = 1)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name= "ROLEID", updatable = false, nullable = false)
    private Long id;
    
    @Column(name= "NAME")
    private String name;
    
    @Column(name= "STATUS")
    private String status;
    
    @Convert(converter = LocalDateTimeConverter.class)
    @Column(name = "ADD_TS")
    protected LocalDateTime addTs;

    @Convert(converter = LocalDateTimeConverter.class)
    @Column(name = "UPD_TS")
    protected LocalDateTime updTs;

    public RoleEntity(){
    }
    
    public RoleEntity(Long id,String name,String status){
        this.id = id;
        this.name = name;
        this.status = status;
    }
    
    @PrePersist
    public void prePersist() {
        addTs = updTs = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        updTs = LocalDateTime.now();
    }
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    
    public LocalDateTime getAddTs() {
        return addTs;
    }

    public void setAddTs(LocalDateTime addTs) {
        this.addTs = addTs;
    }

    public LocalDateTime getUpdTs() {
        return updTs;
    }

    public void setUpdTs(LocalDateTime updTs) {
        this.updTs = updTs;
    }
    
    @Override
    public String toString() {
        return "org.examples.sb.repositories.entities.RoleEntity[ id=" + id + " ]";
    }
}

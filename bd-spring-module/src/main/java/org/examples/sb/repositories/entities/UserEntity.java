/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.examples.sb.repositories.entities;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import jakarta.persistence.*;

/**
 *
 * @author brijeshdhaker
 */
@Entity
@Table(name= "USERS")
public class UserEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    
    //@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "USERS_SEQ_GENERATOR")
    //@SequenceGenerator(name= "USERS_SEQ_GENERATOR", sequenceName = "USERS_SEQUENCE", schema = "PUBLIC", allocationSize = 1)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    // @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name= "USERID", updatable = false, nullable = false)
    private Long id;
   
    @Column(name= "USERNAME")
    private String name;
   
    @Column(name= "EMAIL")
    private String email;
    
    @Column(name= "STATUS")
    private String status;

    //@ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @OneToMany(cascade = CascadeType.MERGE)
    @JoinTable(
        name = "USER_ROLES",
        joinColumns = @JoinColumn(name="USERID", referencedColumnName = "USERID"),
        inverseJoinColumns = @JoinColumn(name="ROLEID", referencedColumnName = "ROLEID")
    )
    private Set<RoleEntity> roles = new HashSet<>();;
    
    @Convert(converter = SystemDateTimeConverter.class)
    @Column(name = "ADD_TS")
    protected Long addTs;

    @Convert(converter = SystemDateTimeConverter.class)
    @Column(name = "UPD_TS")
    protected Long updTs;

    @PrePersist
    public void prePersist() {
        addTs = updTs = System.currentTimeMillis();
    }

    @PreUpdate
    public void preUpdate() {
        updTs = System.currentTimeMillis();
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Set<RoleEntity> getRoles() {
        return roles;
    }

    public void setRoles(Set<RoleEntity> roles) {
        this.roles = roles;
    }

    public void addRole(RoleEntity role) {
        this.roles.add(role);
    }

    public Long getAddTs() {
        return addTs;
    }

    public void setAddTs(Long addTs) {
        this.addTs = addTs;
    }

    public Long getUpdTs() {
        return updTs;
    }

    public void setUpdTs(Long updTs) {
        this.updTs = updTs;
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof UserEntity)) {
            return false;
        }
        UserEntity other = (UserEntity) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.examples.sb.repositories.entities.UserEntity[ id=" + id + " ]";
    }
    
}

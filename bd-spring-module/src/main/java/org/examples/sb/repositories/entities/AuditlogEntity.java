package org.examples.sb.repositories.entities;

import jakarta.persistence.*;
import org.examples.sb.enums.AuditType;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 *
 * @author brijeshdhaker
 */
//@Entity
//@Table(name = "AUDITLOG")
public class AuditlogEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    //@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "AUDITLOG_SEQ_GENERATOR")
    //@SequenceGenerator(name= "AUDITLOG_SEQ_GENERATOR", sequenceName = "HIBERNATE_SEQUENCE", schema = "PUBLIC", allocationSize = 1)
    //@GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", updatable = false, nullable = false)
    private Long id;

    //@OneToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    //@JoinColumn( name = "AUDIT_TYPE", referencedColumnName = "AUDIT_TYPE")
    @Transient
    private AuditType auditType;

    @Column(name = "AUDIT_TYPE")
    private String auditTypeName;

    @Column(name = "ACTION")
    private String logAction;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "REF_ID")
    private String refrenceId;

    @Column(name = "REF_TYPE")
    private String referenceType;

    @Column(name = "USERID")
    private String userid;

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

    @PostLoad
    public void fixupEmnums() {
        this.auditType = AuditType.getByName(auditTypeName);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AuditType getAuditType() {
        return auditType;
    }

    public void setAuditType(AuditType auditType) {
        this.auditType = auditType;
        this.auditTypeName = auditType == null ? "" : auditType.getName();
    }

    public String getLogAction() {
        return logAction;
    }

    public void setLogAction(String logAction) {
        this.logAction = logAction;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRefrenceId() {
        return refrenceId;
    }

    public void setRefrenceId(String refrenceId) {
        this.refrenceId = refrenceId;
    }

    public String getReferenceType() {
        return referenceType;
    }

    public void setReferenceType(String referenceType) {
        this.referenceType = referenceType;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getAuditTypeName() {
        return auditTypeName;
    }

    public void setAuditTypeName(String auditTypeName) {
        this.auditTypeName = auditTypeName;
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
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AuditlogEntity)) {
            return false;
        }
        AuditlogEntity other = (AuditlogEntity) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.example.samples.domain.AuditLog[ id=" + id + " ]";
    }

}

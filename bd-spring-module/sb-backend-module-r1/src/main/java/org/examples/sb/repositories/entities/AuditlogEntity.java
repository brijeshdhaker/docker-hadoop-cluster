package org.examples.sb.repositories.entities;

import jakarta.persistence.*;
import org.examples.sb.enums.AuditType;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 *
 * @author brijeshdhaker
 */
@Entity
@Table(name = "AUDITLOGS")
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

    @Column(name = "LOG_ACTION")
    private String logAction;

    @Column(name = "LOG_MESSAGE")
    private String logMessage;

    @Column(name = "REF_ID")
    private String referenceId;

    @Column(name = "REF_DATA")
    private String referenceData;

    @Column(name = "USERID")
    private String userid;

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

    public String getLogMessage() {
        return logMessage;
    }

    public void setLogMessage(String description) {
        this.logMessage = description;
    }

    public String getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(String referenceId) {
        this.referenceId = referenceId;
    }

    public String getReferenceData() {
        return referenceData;
    }

    public void setReferenceData(String referenceData) {
        this.referenceData = referenceData;
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
        return "org.examples.sb.repositories.entities.AuditlogEntity[ id=" + id + " ]";
    }

}

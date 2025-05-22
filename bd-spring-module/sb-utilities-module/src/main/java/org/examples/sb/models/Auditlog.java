package org.examples.sb.models;

import lombok.Data;

import java.io.Serializable;

@Data
public class Auditlog implements Serializable {

    private Long id;
    private String uuid;
    private String userid;
    private String auditType;
    private String logAction;
    private String logMessage;
    private String referenceId;
    private String referenceData;
    private Long addTs;
    private Long updTs;
}

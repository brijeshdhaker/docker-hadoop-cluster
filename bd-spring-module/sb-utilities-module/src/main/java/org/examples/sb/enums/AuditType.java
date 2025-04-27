/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.examples.sb.enums;

/**
 *
 * @author brijeshdhaker
 */
public enum AuditType implements DBEnumeration<Long> {

    SYSTEM(1, "SYSTEM", "System"),
    BATCH(2, "BATCH", "Batch"),
    USER(3, "USER", "User");

    private long id;
    private String name;
    private String description;

    AuditType(long id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public static AuditType getByCode(long id) {
        for (AuditType type : AuditType.values()) {
            if (type.getId() == id) {
                return type;
            }
        }
        return null;
    }

    public static AuditType getByName(String name) {
        for (AuditType type : AuditType.values()) {
            if (type.getName().equalsIgnoreCase(name)) {
                return type;
            }
        }
        return null;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public Long getDBCode() {
        return id;
    }

}

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
public enum RoleTypes implements DBEnumeration<Long> {

    SYSTEM(1L, "System", "Active"),
    ADMIN(2L, "Admin", "Active"),
    TECHNOLOGY(3L, "Technology", "Active"),
    RISK(4L, "Risk", "Active"),
    AUDIT(5L, "Audit", "Active"),
    CONNECT(6L, "Connect", "Active");;

    private final long id;
    private final String name;
    private final String status;

    RoleTypes(long id, String name, String status) {
        this.id = id;
        this.name = name;
        this.status = status;
    }

    public static RoleTypes getByCode(long id) {
        for (RoleTypes type : RoleTypes.values()) {
            if (type.getId() == id) {
                return type;
            }
        }
        return null;
    }

    public static RoleTypes getByName(String name) {
        for (RoleTypes type : RoleTypes.values()) {
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

    public String getStatus() {
        return status;
    }
    
    @Override
    public Long getDBCode() {
        return id;
    }

}
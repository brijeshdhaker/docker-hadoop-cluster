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
public enum MessageType {

    INFO(1, "info"),
    WARNING(1, "warning"),
    ERROR(1, "error");

    private final Integer code;
    private final String name;

    MessageType(final Integer code, final String name) {
        this.code = code;
        this.name = name;
    }

    public Integer getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

}

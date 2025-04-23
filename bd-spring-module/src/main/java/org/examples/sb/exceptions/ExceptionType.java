/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.examples.sb.exceptions;

/**
 *
 * @author brijeshdhaker
 */
public enum ExceptionType {
    SERVICE_EXCEPTION(100, "Service Exception"),
    REPOSITORY_EXCEPTION(200, "Service Exception");

    private final Integer typeCode;

    private final String typeName;

    private ExceptionType(Integer typeCode, String typeName) {
        this.typeCode = typeCode;
        this.typeName = typeName;
    }

}

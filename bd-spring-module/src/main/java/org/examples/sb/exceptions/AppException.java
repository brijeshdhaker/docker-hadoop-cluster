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
public class AppException extends Exception {

    public AppException(String message) {
        super(message);
    }

    public AppException(String message, Throwable cause) {
        super(message, cause);
    }

    public AppException(ExceptionType exceptionType, String message) {
        super(exceptionType.name() + "::" + message);
    }

    public AppException(ExceptionType exceptionType, String message, Throwable cause) {
        super(exceptionType.name() + "::" + message, cause);
    }

}

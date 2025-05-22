/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.examples.sb.models;

import org.examples.sb.enums.MessageType;

import java.io.Serializable;

/**
 *
 * @author brijeshdhaker
 */
public class MessageDetail implements Serializable {
    
    private MessageType type;
    private String message;
    
    
}

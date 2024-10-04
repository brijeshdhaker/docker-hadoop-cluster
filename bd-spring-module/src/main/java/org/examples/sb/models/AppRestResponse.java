/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.examples.sb.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author brijeshdhaker
 */
public class AppRestResponse implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private Object data;
    private Integer count;
    private String message;
    private Boolean success;
    
    private List<MessageDetail> messageDetails = new ArrayList<>();

    public AppRestResponse() {
    }

    public AppRestResponse(Object data) {
        if(data != null){
            this.data = data;
            this.success = Boolean.TRUE;
        }
    }
    
    public AppRestResponse(Exception e, String message) {
        this.success = Boolean.FALSE;
        if(null != message){
            this.message = message;
        }else{
            this.message = e.getMessage();
        }
        
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public List<MessageDetail> getMessageDetails() {
        return messageDetails;
    }

    public void setMessageDetails(List<MessageDetail> messageDetails) {
        this.messageDetails = messageDetails;
    }
    
}

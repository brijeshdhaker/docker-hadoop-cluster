package com.org.example.design.factory;

import com.org.example.design.model.Computer;
import com.org.example.design.model.Desktop;
import com.org.example.design.model.Server;

public class ComputerFactory {

    public static Computer getComputer(String type){
        if("Desktop".equalsIgnoreCase(type)) {
            return new Desktop();
        }else if("Server".equalsIgnoreCase(type)){
            return new Server();
        }
        return null;
    }


}

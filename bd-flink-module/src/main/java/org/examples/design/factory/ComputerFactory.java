package org.examples.design.factory;

import org.examples.design.model.Computer;
import org.examples.design.model.Desktop;
import org.examples.design.model.Server;

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

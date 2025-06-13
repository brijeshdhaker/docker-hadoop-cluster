package com.org.example.design.abstractfactory;

import com.org.example.design.model.Computer;
import com.org.example.design.model.ComputerSpecification;
import com.org.example.design.model.Desktop;
import com.org.example.design.model.Server;

public class TestDesignPatterns {

    public static void main(String[] args) {

        ComputerFactory desktopFactory = testAbstractFactory("Desktop");
        System.out.println("Computer Factory Type : " + desktopFactory.getFactoryType());
        Computer desktop = desktopFactory.createComputer();
        System.out.println("Computer Type : " + desktop.getType());
        desktop.assemble();
        ComputerSpecification desktopSpecification = desktopFactory.displaySpecification();
        desktopSpecification.display();

        //
        ComputerFactory serverFactory = testAbstractFactory("Server");
        System.out.println("Computer Factory Type : " + serverFactory.getFactoryType());
        Computer server = serverFactory.createComputer();
        System.out.println("Computer Type : " + server.getType());
        server.assemble();
        ComputerSpecification serverSpecification = serverFactory.displaySpecification();
        serverSpecification.display();

    }

    private static ComputerFactory testAbstractFactory(String type) {
        ComputerFactory factory = null;
        if("Desktop".equalsIgnoreCase(type)) {
            factory = new DesktopFactory();
        }else if("Server".equalsIgnoreCase(type)){
            factory = new ServerFactory();
        }
        return factory;
    }
}

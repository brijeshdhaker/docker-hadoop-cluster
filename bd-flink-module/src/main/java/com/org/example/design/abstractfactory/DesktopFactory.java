package com.org.example.design.abstractfactory;

import com.org.example.design.model.Computer;
import com.org.example.design.model.ComputerSpecification;
import com.org.example.design.model.Desktop;
import com.org.example.design.model.DesktopSpecification;

public class DesktopFactory extends ComputerFactory {

    public DesktopFactory() {
        super("Desktop Factory");
    }

    @Override
    public Computer createComputer() {
        return new Desktop();
    }

    @Override
    public ComputerSpecification displaySpecification() {
        return new DesktopSpecification("16GB","8 Core","1 TiB");
    }
}

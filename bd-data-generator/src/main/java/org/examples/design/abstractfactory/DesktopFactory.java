package org.examples.design.abstractfactory;

import org.examples.design.model.Computer;
import org.examples.design.model.ComputerSpecification;
import org.examples.design.model.Desktop;
import org.examples.design.model.DesktopSpecification;

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

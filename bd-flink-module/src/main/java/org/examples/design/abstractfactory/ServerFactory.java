package org.examples.design.abstractfactory;

import org.examples.design.model.*;

public class ServerFactory extends ComputerFactory {

    public ServerFactory() {
        super("Server Factory");
    }

    @Override
    public Computer createComputer() {
        return new Server();
    }

    @Override
    public ComputerSpecification displaySpecification() {
        return new ServerSpecification("64GB","32 Core","8 TiB");
    }
}

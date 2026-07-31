package ictinfrastructures.lab09;

import org.cloudsimplus.core.CloudSimPlus;
import org.cloudsimplus.datacenters.Datacenter;

public class Exercise1 {

    public static void main(String[] args) {

        CloudSimPlus simulation = new CloudSimPlus();

        // Creating the datacenter infrastructure
        Datacenter datacenter = Lab.createDatacenter(simulation);

        // Adding hosts
        // TODO
        // e.g., datacenter.addHost(Lab.createHost(8, 16_384, 2_000_000, 10_000));

        // Visualising the overall available computing resources
        Lab.printDatacenterInfo(datacenter);
    }
}
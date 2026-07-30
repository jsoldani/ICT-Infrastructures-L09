package ictinfrastructures.lab09;

import org.cloudsimplus.brokers.DatacenterBroker;
import org.cloudsimplus.core.CloudSimPlus;
import org.cloudsimplus.datacenters.Datacenter;

public class Exercise1 {

    public static void main(String[] args) {

        System.out.println("ICT Infrastructure: Hands-on Lab (with CloudSimPlus)");

        CloudSimPlus simulation = new CloudSimPlus();

        Datacenter datacenter = Lab.createDatacenter(simulation);
        DatacenterBroker broker = Lab.createBroker(simulation);

        // TODO: ADD HOSTS
        // e.g., datacenter.addHost(Lab.createHost(8, 16_384, 2_000_000, 10_000));
        
        Lab.printDatacenterInfo(datacenter);
    }
}
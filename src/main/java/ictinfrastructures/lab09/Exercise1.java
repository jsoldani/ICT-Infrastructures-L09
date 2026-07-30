package ictinfrastructures.lab09;

import java.util.ArrayList;
import java.util.List;

import org.cloudsimplus.brokers.DatacenterBroker;
import org.cloudsimplus.core.CloudSimPlus;
import org.cloudsimplus.datacenters.Datacenter;
import org.cloudsimplus.vms.Vm;

public class Exercise1 {

    public static void main(String[] args) {

        CloudSimPlus simulation = new CloudSimPlus();

        // Creating the datacenter infrastructure
        Datacenter datacenter = Lab.createDatacenter(simulation);
        // TODO: ADD HOSTS
        // e.g., datacenter.addHost(Lab.createHost(8, 16_384, 2_000_000, 10_000));
        Lab.printDatacenterInfo(datacenter);

        // Creating a broker to place VMs over the datacenter infrastructure
        DatacenterBroker broker = Lab.createBroker(simulation);

        // Submitting VMs for placement
        List<Vm> vmList = new ArrayList();
        // TODO: ADD VMs
        // e.g., vmList.add(Lab.createVM(2, 2048, 1_000_000, 5_000));
        broker.submitVmList(vmList);

        // Simulating the VM placement
        simulation.terminateAt(1);
        simulation.start();

        // Visualising the result (-1 means that the VM was not placed)
        Lab.printVMAllocation(vmList);
    }
}
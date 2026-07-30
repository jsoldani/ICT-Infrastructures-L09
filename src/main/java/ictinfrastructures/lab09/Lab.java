package ictinfrastructures.lab09;

import org.cloudsimplus.allocationpolicies.VmAllocationPolicySimple;
import org.cloudsimplus.core.CloudSimPlus;
import org.cloudsimplus.datacenters.Datacenter;
import org.cloudsimplus.datacenters.DatacenterSimple;
import org.cloudsimplus.hosts.Host;
import org.cloudsimplus.resources.Pe;
import org.cloudsimplus.schedulers.vm.VmSchedulerSpaceShared;

import java.util.ArrayList;
import java.util.List;

public class Lab {

    public static void main(String[] args) {

        System.out.println("Starting CloudSim...");

        CloudSimPlus simulation = new CloudSimPlus();

        Datacenter datacenter = createDatacenter(simulation);

        System.out.printf(
                "Data center successfully created with %d host(s)%n",
                datacenter.getHostList().size()
        );

        System.out.println("Simulation finished.");
    }

    /**
     * ===========================================================
     * TODO (Assignment 1)
     *
     * Create a Data Center composed of FOUR identical hosts.
     *
     * Each host must have:
     *
     * - 8 CPU cores
     * - 2000 MIPS per core
     * - 16 GB RAM
     * - 2 TB Storage
     * - 10 Gbps Bandwidth
     *
     * Return the created Datacenter object.
     * ===========================================================
     */
    private static Datacenter createDatacenter(CloudSimPlus simulation) {

        List<Host> hostList = new ArrayList<>();


        /*
         * -------------------------------------------------------
         * TODO:
         *
         * 1. Create four Host objects.
         *
         * 2. Add each Host to hostList.
         *
         * 3. Return a DatacenterSimple using hostList.
         * -------------------------------------------------------
         */


        return new DatacenterSimple(
                simulation,
                hostList,
                new VmAllocationPolicySimple()
        );
    }

}
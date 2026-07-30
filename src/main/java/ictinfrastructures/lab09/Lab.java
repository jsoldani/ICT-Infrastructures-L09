package ictinfrastructures.lab09;

import java.util.ArrayList;
import java.util.List;

import org.cloudsimplus.allocationpolicies.VmAllocationPolicySimple;
import org.cloudsimplus.brokers.DatacenterBroker;
import org.cloudsimplus.brokers.DatacenterBrokerSimple;
import org.cloudsimplus.core.CloudSimPlus;
import org.cloudsimplus.datacenters.Datacenter;
import org.cloudsimplus.datacenters.DatacenterSimple;
import org.cloudsimplus.hosts.Host;
import org.cloudsimplus.hosts.HostSimple;
import org.cloudsimplus.resources.Pe;
import org.cloudsimplus.resources.PeSimple;
import org.cloudsimplus.schedulers.vm.VmSchedulerSpaceShared;
import org.cloudsimplus.vms.Vm;
import org.cloudsimplus.vms.VmSimple;

public class Lab {
    // By default, hosts' and VMs' cores are with 2000 MIPS
    private static final int MIPS = 2000;

    // Helper method to create a data center where to run the simulation
    public static Datacenter createDatacenter(CloudSimPlus simulation) {
        return new DatacenterSimple(simulation,new ArrayList<Host>(),new VmAllocationPolicySimple());
    }

    // Helper method to print data center info
    public static void printDatacenterInfo(Datacenter d) {
        // Compute total resources
        long totalRam = 0;
        long totalStorage = 0;
        long totalCores = 0;
        for (Host host : d.getHostList()) {
            totalRam += host.getRam().getCapacity();
            totalStorage += host.getStorage().getCapacity();
            totalCores += host.getPesNumber();
        }
        // Print resources (more human readable)
        System.out.println("Hosts      : " + d.getHostList().size());
        System.out.println("CPU cores  : " + totalCores);
        System.out.println("RAM (GB)   : " + totalRam/1024.0);
        System.out.println("Storage(TB): " + totalStorage/1_048_576.0);
    }

    // Helper method to create a broker to submit VMs to a data center
    public static DatacenterBroker createBroker(CloudSimPlus simulation) {
        return new DatacenterBrokerSimple(simulation);
    }

    // Helper method to create hosts of a data center
    // (RAM and storage are in MBs, bandwidth is in Mb/s)
    public static Host createHost(int cores, long ram, long storage, long bandwidth) {
        // Creating CPU cores
        List<Pe> coreList = new ArrayList<>();
        for (int i = 0; i < cores; i++) {
            coreList.add(new PeSimple(MIPS));
        }
        // Creating host 
        Host host = new HostSimple(ram,bandwidth,storage,coreList); 
        // Configuring host to allocate physical cores exclusively to VMs
        // (change to VmSchedulerTimeShared to share fractions of available cores concurrently among active VMs)
        host.setVmScheduler(new VmSchedulerSpaceShared());
        // Returning host
        return host;
    }

    // Helper method to create a VM
    // (RAM and storage are in MBs, bandwidth is in Mb/s)
    public static Vm createVM(int cores, long ram, long storage, long bandwidth) {
        final Vm vm = new VmSimple(MIPS,cores);
        vm.setRam(ram);
        vm.setSize(storage);
        vm.setBw(bandwidth);
        return vm;
    }
}

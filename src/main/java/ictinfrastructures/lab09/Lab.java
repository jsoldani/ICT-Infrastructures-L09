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
    // Utils for conversion
    private static final double MB_TO_GB = 1024.0;
    private static final double MB_TO_TB = 1024.0 * 1024.0;

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
        System.out.println("\n============== Datacenter info ==============");
        System.out.println("Hosts      : " + d.getHostList().size());
        System.out.println("CPU cores  : " + totalCores);
        System.out.println("RAM (GB)   : " + totalRam/MB_TO_GB);
        System.out.println("Storage(TB): " + totalStorage/MB_TO_TB);
        System.out.println("===============================================");
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

    // Helper method to print the allocation of VMs
    public static void printVMAllocation(List<Vm> vmList) {

        System.out.println("\n================ VM Allocation ================");

        System.out.printf("%-5s %-6s %-8s %-8s %-10s%n",
                "VM", "Host", "vCPUs", "RAM", "Storage");

        for (Vm vm : vmList) {

            System.out.printf("%-5d %-6d %-8d %-8d %-10d%n",
                    vm.getId(),
                    vm.getHost().getId(),
                    vm.getPesNumber(),
                    vm.getRam().getCapacity(),
                    vm.getStorage().getCapacity());
        }

        System.out.println("===============================================");
    }

    // Helper method to print the actual utilization of hosts
    public static void printHostUtilization(Datacenter dc) {

        System.out.println("\n============= Host Utilization =============");

        for (Host host : dc.getHostList()) {

            System.out.printf(
                "Host %2d: %d/%d cores, %.1f GB RAM used%n",
                host.getId(),
                host.getBusyPesNumber(),
                host.getPesNumber(),
                host.getRam().getAllocatedResource()/1024.0);
        }

        System.out.println("============================================");
    }
}

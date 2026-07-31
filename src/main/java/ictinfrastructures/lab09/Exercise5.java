package ictinfrastructures.lab09;

import java.util.ArrayList;
import java.util.List;

import org.cloudsimplus.cloudlets.Cloudlet;
import org.cloudsimplus.cloudlets.CloudletSimple;
import org.cloudsimplus.power.PowerMeasurement;
import org.cloudsimplus.power.PowerMeter;
import org.cloudsimplus.power.models.PowerModelHostSimple;
import org.cloudsimplus.utilizationmodels.UtilizationModelDynamic;

import org.cloudsimplus.brokers.DatacenterBroker;
import org.cloudsimplus.core.CloudSimPlus;
import org.cloudsimplus.datacenters.Datacenter;
import org.cloudsimplus.hosts.Host;
import org.cloudsimplus.vms.Vm;

public class Exercise5 {

    // Simulation lasts 1 simulated day
    private static final double SIMULATION_TIME = 24 * 3600;

    // Workload CPU utilization
    private static final double UTILIZATION = 0.50;

    // Number of physical hosts
    private static final int NUMBER_OF_HOSTS = 10;

    // Number of VMs
    private static final int NUMBER_OF_VMS = 50;

    // Power monitoring interval
    private static final int MONITORING_INTERVAL = 300;

    public static void main(String[] args) {

        // Create simulator
        CloudSimPlus simulation = new CloudSimPlus();

        // Create datacenter
        Datacenter datacenter = Lab.createDatacenter(simulation);

        // Creating hosts (all with same specs)
        int h_cores = 16;
        long h_ram = 64 * 1024; // 64 GB
        long h_storage = 2 * 1024 * 1024; // 2 TB
        long h_bandwidth = 10_000; // 10 Gb/s
        int h_idlePower = 180; // 180 W at 0% load
        int h_maxPower = 450; // 450 W at 100% load
        
        List<Host> hostList = new ArrayList<>();
        for (int i = 0; i < NUMBER_OF_HOSTS; i++) {
            Host host = Lab.createHost(h_cores, h_ram, h_storage, h_bandwidth);
            // Setting host to consume power linearly (450 W at 0% load, 180 W at 100% load)
            host.setPowerModel(new PowerModelHostSimple(h_maxPower,h_idlePower));
            hostList.add(host);
        }
        datacenter.addHostList(hostList);

        // Creating VMs (all with same specs)
        int vm_cores = 2;
        long vm_ram = 4 * 1024; // 4 GB
        long vm_storage = 40 * 1024; // 40 GB
        long vm_bandwidth = 1_000; // 1 Gb/s

        List<Vm> vmList = new ArrayList<>();
        for (int i = 0; i < NUMBER_OF_VMS; i++) {
            Vm vm = Lab.createVM(vm_cores,vm_ram,vm_storage,vm_bandwidth);
            vmList.add(vm);
        }

        // Creating broker to submit VMs and workload to datacenter
        DatacenterBroker broker = Lab.createBroker(simulation);
        
        // Submitting VMs to the datacenter 
        broker.submitVmList(vmList);

        // Configuring workload (one cloudlet for each VM, with UTILIZATION, lasting longer than 1 year simulation)
        List<Cloudlet> cloudletList = new ArrayList<>();
        for (Vm vm : vmList) {
            UtilizationModelDynamic utilizationModel = new UtilizationModelDynamic(UTILIZATION);
            Cloudlet cloudlet = new CloudletSimple(10_000_000, (int) vm.getPesNumber(), utilizationModel);
            cloudlet.setVm(vm);
            cloudletList.add(cloudlet);
        }
        broker.submitCloudletList(cloudletList);

        // Creating power meter to measure power consumption every simulated MEASUREMENT_INTERVAL
        PowerMeter powerMeter = new PowerMeter(simulation, hostList);
        powerMeter.setMeasurementInterval(MONITORING_INTERVAL);

        // Running the simulation for SIMULATION_TIME
        simulation.terminateAt(SIMULATION_TIME);
        simulation.start();

        // Print VM allocation
        Lab.printVMAllocation(vmList);

        // Computing and printing overall energy consumption
        List<PowerMeasurement> measurements = powerMeter.getPowerMeasurements();

        double averagePower = 0;
        for (PowerMeasurement measurement : measurements) {
            averagePower += measurement.getTotalPower();
        }
        averagePower = averagePower / measurements.size();

        double energyWh = averagePower * SIMULATION_TIME / 3600;
        double energyKWh = energyWh / 1000;

        System.out.println();
        System.out.println("============== Energy Consumption ==============");

        System.out.printf(
                "Measurement samples : %d%n",
                measurements.size());

        System.out.printf(
                "Average power      : %.2f W%n",
                averagePower);

        System.out.printf(
                "Energy consumed    : %.4f kWh%n",
                energyKWh);

        System.out.println("================================================");
    }
}
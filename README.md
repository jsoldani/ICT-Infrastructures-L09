# Lab 09 

This lab is intended to experiment with some of the notions acquired in the first part of the **ICT Infrastructures** course. 

# Running exercises

For each assigned exercise N, you will find a `ExerciseN.java` class in the folder [src/main/java/ictinfrastructures/lab09](src/main/java/ictinfrastructures/lab09). Fill the class with the code required to run the required simulation.

The simulation developed for an exercise can then be run by issuing the following command: 
```
python3 run.py N
```
where `N` is the number of the exercise you wish to run.

_Note: Requires Python 3.8 and Java 17._

# Helper class
The simulations are based on the [CloudSimPlus](https://cloudsimplus.org) simulator. To abstract away from the boilerplate code needed to configure and run them, a helper [Lab.java](src/main/java/ictinfrastructures/lab09/java) class is made available. The helper class allows to:
- Create data centers
- Create brokers
- Create hosts
- Create virtual machines (VMs)
- Inspect the simulated infrastructure
- Print VM placement
- Monitor host resource utilization

## `createDatacenter()`

```java
public static Datacenter createDatacenter(CloudSimPlus simulation)
```

Creates an empty data center that can later be populated with hosts.

Internally, the method creates:

- an empty host list
- a `VmAllocationPolicySimple`

Example:

```java
CloudSimPlus simulation = new CloudSimPlus();

Datacenter dc = Lab.createDatacenter(simulation);

dc.addHost(Lab.createHost(...));
dc.addHost(Lab.createHost(...));
```

---

## `printDatacenterInfo()`

```java
public static void printDatacenterInfo(Datacenter d)
```

Prints a summary of the available resources in the data center.

The method computes:

- number of hosts
- total CPU cores
- total RAM
- total storage

Example output:

```
============== Datacenter info ==============
Hosts      : 4
CPU cores  : 32
RAM (GB)   : 128.0
Storage(TB): 8.0
=============================================
```

This method is useful to verify that the infrastructure has been configured correctly before starting the simulation.

---

## `createBroker()`

```java
public static DatacenterBroker createBroker(CloudSimPlus simulation)
```

Creates a broker.

The broker is responsible for:

- submitting VMs to the data center
- submitting Cloudlets to the VMs
- managing the execution of the simulation

Example:

```java
DatacenterBroker broker = Lab.createBroker(simulation);

broker.submitVmList(vmList);
broker.submitCloudletList(cloudletList);
```

---

## `createHost()`

```java
public static Host createHost(
    int cores,
    long ram,
    long storage,
    long bandwidth)
```

Creates a physical server (host).

Parameters:

| Parameter | Description |
|------------|-------------|
| `cores` | Number of CPU cores |
| `ram` | RAM (MB) |
| `storage` | Storage (MB) |
| `bandwidth` | Network bandwidth (Mb/s) |

Each CPU core provides:

```
2000 MIPS
```

Internally, the method:

1. creates one `PeSimple` object for each CPU core;
2. creates a `HostSimple`;
3. configures a **SpaceShared VM Scheduler**.

Example:

```java
Host host = Lab.createHost(
    8,
    16384,
    2000000,
    10000
);
```

---

### SpaceShared Scheduling

The helper configures

```java
VmSchedulerSpaceShared
```

This scheduling policy allocates **whole CPU cores** to VMs.

For example, on an 8-core host:

- VM A requests 4 cores
- VM B requests 2 cores

Both VMs can run simultaneously.

If another VM requests 4 cores, it must wait until enough cores become available.

An alternative scheduler is

```java
VmSchedulerTimeShared
```

which allows several VMs to share the same physical core.

---

## `createVM()`

```java
public static Vm createVM(
    int cores,
    long ram,
    long storage,
    long bandwidth)
```

Creates a virtual machine.

Parameters:

| Parameter | Description |
|------------|-------------|
| `cores` | Number of virtual CPUs |
| `ram` | RAM (MB) |
| `storage` | Storage (MB) |
| `bandwidth` | Network bandwidth (Mb/s) |

Each virtual CPU is configured with

```
2000 MIPS
```

Example:

```java
Vm vm = Lab.createVM(
    2,
    4096,
    50000,
    1000
);
```

---

## `printVMAllocation()`

```java
public static void printVMAllocation(List<Vm> vmList)
```

Prints where each VM has been placed.

Displayed information:

- VM identifier
- Host identifier
- Number of virtual CPUs
- RAM
- Storage

Example output:

```
================ VM Allocation ================

VM    Host   vCPUs    RAM      Storage
0     1      2        4096     50000
1     0      4        8192     100000
2     1      1        2048     20000

===============================================
```

This method is useful for understanding how the allocation policy distributes VMs across the hosts.

---

## `printHostUtilization()`

```java
public static void printHostUtilization(Datacenter dc)
```

Prints the current utilization of each host.

Displayed information:

- Host identifier
- Busy CPU cores
- Total CPU cores
- Allocated RAM

Example output:

```
============= Host Utilization =============

Host  0: 4/8 cores, 8.0 GB RAM used
Host  1: 6/8 cores, 12.0 GB RAM used
Host  2: 0/8 cores, 0.0 GB RAM used

============================================
```

This method helps students understand:

- how heavily each host is loaded;
- whether the infrastructure is balanced;
- whether additional hosts are needed.
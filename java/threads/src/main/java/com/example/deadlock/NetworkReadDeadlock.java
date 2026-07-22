package com.example.deadlock;

public class NetworkReadDeadlock {

    public static void main(String[] args) {
        // Instantiate normal, non-static heap objects
        NetworkReader networkEngine = new NetworkReader("Eth0-Input-Buffer");
        DiskWriter diskEngine = new DiskWriter("SSD-Storage-Pool");

        // Thread 1: Initiates pipeline from Network -> Disk
        Thread t1 = new Thread(() -> {
            networkEngine.streamToDisk(diskEngine);
        }, "Network-To-Disk-Thread");

        // Thread 2: Initiates pipeline from Disk -> Network (e.g., diagnostic check/flush)
        Thread t2 = new Thread(() -> {
            diskEngine.streamToNetwork(networkEngine);
        }, "Disk-To-Network-Thread");

        t1.start();
        t2.start();
    }
}

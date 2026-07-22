package com.example.deadlock;


public class DiskWriter {
    private final String diskName;

    public DiskWriter(String diskName) {
        this.diskName = diskName;
    }

    public String getDiskName() {
        return diskName;
    }

    // This method handles writing to disk and sending a status back to the network engine
    public synchronized void streamToNetwork(NetworkReader reader) {
        System.out.println(Thread.currentThread().getName() + " locked disk resource: " + this.diskName);

        try {
            Thread.sleep(50); // Simulate disk I/O lag
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println(Thread.currentThread().getName() + " waiting to lock network: " + reader.getSourceName());

        // Circular dependency occurs here when calling a synchronized method on the reader
        reader.receiveStatusUpdate();
    }

    // Call-back method used by the NetworkReader
    public synchronized void receiveNetworkData(NetworkReader reader) {
        System.out.println("Data successfully written to " + this.diskName);
    }
}


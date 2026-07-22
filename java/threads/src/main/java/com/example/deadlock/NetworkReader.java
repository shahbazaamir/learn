package com.example.deadlock;


public class NetworkReader {
    private final String sourceName;

    public NetworkReader(String sourceName) {
        this.sourceName = sourceName;
    }

    public String getSourceName() {
        return sourceName;
    }

    // This method handles pulling data from the network and archiving it to disk
    public synchronized void streamToDisk(DiskWriter writer) {
        System.out.println(Thread.currentThread().getName() + " locked network resource: " + this.sourceName);

        try {
            Thread.sleep(50); // Simulate network latency
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println(Thread.currentThread().getName() + " waiting to lock disk: " + writer.getDiskName());

        // Circular dependency occurs here when calling a synchronized method on the writer
        writer.receiveNetworkData(this);
    }

    // Call-back method used by the DiskWriter
    public synchronized void receiveStatusUpdate() {
        System.out.println("Status update received by " + this.sourceName);
    }
}


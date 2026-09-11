package com.example.deadlock;

public class BankDeadLockExample {

    static class BankAccount {
        private final String name;
        private double balance;

        public BankAccount(String name, double balance) {
            this.name = name;
            this.balance = balance;
        }

        public String getName() { return name; }

        // Synchronized transfer method prone to deadlock
        public void transferFunds(BankAccount targetAccount, double amount) {
            // Lock the source account
            synchronized (this) {
                System.out.println(Thread.currentThread().getName() + " LOCKED " + this.getName());

                try {
                    Thread.sleep(50); // Simulates network/database latency
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }

                System.out.println(Thread.currentThread().getName() + " WAITING TO LOCK " + targetAccount.getName());

                // Attempt to lock the target account
                synchronized (targetAccount) {
                    this.balance -= amount;
                    targetAccount.balance += amount;
                    System.out.println("Transfer successful!");
                }
            }
        }
    }

    public static void main(String[] args) throws  Exception{
        BankAccount alice = new BankAccount("Alice's Account", 1000);
        BankAccount bob = new BankAccount("Bob's Account", 2000);

        // Transaction 1: Alice transfers to Bob
        Thread tx1 = new Thread(() -> alice.transferFunds(bob, 100), "Tx-Thread-1");

        // Transaction 2: Bob transfers to Alice at the same time
        Thread tx2 = new Thread(() -> bob.transferFunds(alice, 50), "Tx-Thread-2");

        DeadlockDetector d = new DeadlockDetector();
        d.start();
        Thread.sleep(1000);
        tx1.start();
        tx2.start();
    }


}

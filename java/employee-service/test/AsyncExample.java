import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.Future;
import java.util.concurrent.ExecutorService ;
import java.util.concurrent.Executors ;

/**
 * AsyncExample is the stand alone application  with a main program 
 * calling another class AsyncTask and method performTask in  an asynchronous manner 
 *
 */
public class AsyncExample {
    public static void main(String[] args) {
        System.out.println("Main program starts... starting new thread");
        // Main thread continues to execute other tasks and is not blocked
        //MyThread myThread = new MyThread();
        //myThread.run();
        System.out.println("Main program finishes. Buth thread keeps running");
    }
}


/**
 * showing usage of thread
 */

class MyThread implements Runnable {
    @Override
    public void run(){
	
        CompletableFuture<String> futureResult = AsyncTask.performTask();

        System.out.println("Main program calls child thread using completable future");
        // Attach a callback to handle the result once the task completes
        futureResult.thenAccept(result -> {
            System.out.println("Callback: Result from first child thread: " + result);
	    // Main Thread reacting to callback from child thread 
        });
        System.out.println("Main program continues execution without waiting for child thread...");
        ExecutorService executor = Executors.newSingleThreadExecutor();
        System.out.println("Next, Main program waits for child thread to complete called using future ");
        Future<String> futureTask = executor.submit(() -> {
            System.out.println("Another Child thread starts processing...");
            Thread.sleep(3000);
            System.out.println("Second Child thread completes processing.");
            return "Task Completed!";
        });

        AsyncTask.performTaskWithChain("Hello, CompletionStage!")
            .thenAccept(result -> {
                // Handle the callback (result)
                System.out.println("Callback received: " + result);
            })
            .exceptionally(ex -> {
                // Handle exceptions
                System.err.println("An error occurred: " + ex.getMessage());
                return null;
            });
        }
}

/**
 * This class will be call asynchronously
 */
class AsyncTask {
    /**
     * Method to perform the task asynchronously with chaining
     */
    public static CompletionStage<String> performTaskWithChain( String args) {
    
        return CompletableFuture.supplyAsync(() -> {
            System.out.println("Last Child thread starts processing...");
            try {
                // deliberate sleep in the child thread and after the sleep time 
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            System.out.println("Last Child thread completes processing.");
            return "Last Task Completed!"; // returning a simple string back to main call
        });
    }
    public static CompletableFuture<String> performTask() {
        return CompletableFuture.supplyAsync(() -> {
            System.out.println("First Child thread starts processing...");
            try {
                // deliberate sleep in the child thread and after the sleep time 
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            System.out.println("First Child thread completes processing.");
            return "Task Completed!"; // returning a simple string back to main call
        });
    }
}

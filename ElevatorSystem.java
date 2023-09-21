import java.util.concurrent.*;

public class ElevatorSystem {

    private ElevatorController controller;
    private ElevatorVisualizer visualizer;
    private ExecutorService executor;

    public ElevatorSystem() {
        controller = new ElevatorController();
        visualizer = new ElevatorVisualizer(controller);
        executor = Executors.newFixedThreadPool(10);  // Use a fixed thread pool with 10 threads.
    }

    public Future<?> requestElevator(int fromFloor, int toFloor) {
        return executor.submit(() -> {
            synchronized (controller) {  // Ensure thread safety
                controller.requestElevator(fromFloor);
            }
            try {
                // Simulate waiting for the elevator to arrive
                Thread.sleep(Math.abs(fromFloor - controller.elevators[0].currentFloor) * 1000);  // 1 second per floor
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            synchronized (controller) {  // Ensure thread safety
                controller.requestElevator(toFloor);
            }
            visualizer.updateVisualRepresentation(controller);  // Update the visualization
        });
    }

    public void shutdown() {
        executor.shutdown();
    }

    public static void main(String[] args) {
        ElevatorSystem system = new ElevatorSystem();
Thread request1 = new Thread(() -> system.requestElevator(1, 10));
    Thread request2 = new Thread(() -> system.requestElevator(20, 30));
    Thread request3 = new Thread(() -> system.requestElevator(40, 50));
    Thread request4 = new Thread(() -> system.requestElevator(5, 55));
    Thread request5 = new Thread(() -> system.requestElevator(15, 60));

    // Starting the threads
    request1.start();
    request2.start();
    request3.start();
    request4.start();
    request5.start();

    // Join the threads (optional - if you want the main thread to wait for all requests to complete)
    try {
        request1.join();
        request2.join();
        request3.join();
        request4.join();
        request5.join();
    } catch (InterruptedException e) {
        e.printStackTrace();
    }
        // ... Add more requests if needed

        system.shutdown();
    }
}

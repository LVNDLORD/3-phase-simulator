package model;

// Customers arriving at random intervals using exponential distribution and then the service point retrieves them
// from the queue and serves them at random time using normal distribution.
// then we are able to observe how long the client has stayed in our system (time in queue + service time).
public class Main {

    public static void main(String[] args) {
        System.out.println("Simulator started\n");

        Engine engine = new MyEngine();
        engine.setSimulationTime(1000);
        engine.run();
    }
}

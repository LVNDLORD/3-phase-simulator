package model;
// Simulate a queueing system with one service point and one queue.
// The service time is normally distributed with mean 10.
// The interarrival time is exponentially distributed with mean 15.
// The simulation runs until the number of arrivals is 1000.
// The program prints the average waiting time.
// 2:09:10  - 23.11 (how B-phase works)

import controller.Controller;
import model.eduni.distributions.*;

public class Simulation extends Engine {
    // Actual simulation body
    // Inform controller when simulation's state changes

    private ArrivalProcess arrivalProcess;

    private ServicePoint[] servicePoint;

    private final Controller controller;

    public enum Distributions {
        Normal,
        Uniform,
        Exponential
    }

    /**
     * Simulation constructor
     * @param controller         Controller of the application
     * @param servicePointsCount Number of cashier desks to initialize
     * @param customersCount     ?
     * @param dist               Probability distribution type. Provided by eduni package
     */
    public Simulation(Controller controller, int servicePointsCount, int customersCount, Distributions dist) {
        super();
        this.controller = controller;

        // Create distributions
        ContinuousGenerator spDist;
        ContinuousGenerator apDist;

        switch (dist) {
            case Normal:
                spDist = new Normal(10, 6);
                apDist = new Normal(10, 7);
                break;
            case Uniform:
                spDist = new Uniform(1, 6);
                apDist = new Uniform(2, 7);
                break;
            case Exponential:
                spDist = new Negexp(10, 6);
                apDist = new Negexp(10, 7);
                break;
            default:
                controller.log("Invalid distribution type: " + dist, controller.RED);
                return;
        }

        // Create service points
        servicePoint = new ServicePoint[servicePointsCount];

        for (int i=0; i<servicePointsCount; i++) {
            servicePoint[i] = new ServicePoint("Cashier desk " + (i+1), spDist, eventlist, EventType.DEP);
        }

        arrivalProcess = new ArrivalProcess(apDist, eventlist, EventType.ARR);
    }

    protected void initialize() {
        arrivalProcess.generateNextEvent();
    }

    protected void runEvent(Event e) {
        Customer a;

        switch (e.getType()) {
            case ARR:
                servicePoint[0].addToQueue(new Customer());
                arrivalProcess.generateNextEvent(); // generate next arrival
                break;

            case DEP:
                a = servicePoint[0].removeFromQueue(); // removing customer from the second service point
                // when departure from the second service point is generated
                a.setRemovalTime(Clock.getInstance().getClock()); // setting the removal time for the customer from the system
                a.reportResults();
                break;
        }
    }

    protected void tryCEvents() {
        for (ServicePoint sp : servicePoint) {
            // C event - conditional
            // if sp ready, and there is a customer in the queue, we start the service.
            if (!sp.isReserved() && sp.isOnQueue()) {
                sp.beginService();
            }
        }
    }

    protected void results() {
        System.out.printf("\nSimulation ended at %.2f\n", Clock.getInstance().getClock());
        System.out.println("Total customers served: " + servicePoint[0].getServedCustomers());
        //System.out.printf("Average service time: %.2f\n", servicePoint[0].getMeanServiceTime());
    }

    private void log (String s) {
        System.out.println(s);
    }
}

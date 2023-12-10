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

    private ServicePoint[] servicePoints;

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
    public  Simulation(Controller controller, int servicePointsCount, int customersCount,
                       Distributions dist, double meanSP, double varianceSP, double meanAP, double varianceAP) {
        super();
        this.controller = controller;
        // Create distributions
        ContinuousGenerator spDist = new Normal(0, 1); // Default to Normal distribution with mean 0 and variance 1
        ContinuousGenerator apDist = new Normal(0, 1);


        switch (dist) {
            case Normal:
                // double mean, variance
                spDist = new Normal(meanSP, varianceSP);
                apDist = new Normal(meanAP, varianceAP);
                break;
            case Uniform:
                // ! Max > min!
                // double min, max
                spDist = new Uniform(meanSP, varianceSP); //min, max
                apDist = new Uniform(meanAP, varianceAP); //min, max.
                break;
                // temp comment for dev purpose, as second arg is type long
            case Exponential:
                // double mean, long seed
                spDist = new Negexp(meanSP, (long) varianceSP);
                apDist = new Negexp(meanAP, (long) varianceAP);
                break;
            default:
                controller.log("Invalid distribution type: " + dist, controller.RED);
                return;
        }

        // Create service points
        servicePoints = new ServicePoint[servicePointsCount];

        for (int i=0; i<servicePointsCount; i++) {
            servicePoints[i] = new ServicePoint("Cashier desk " + (i + 1), spDist, eventlist, EventType.DEP, controller::updateQueueInfo, i);
        }
        arrivalProcess = new ArrivalProcess(apDist, eventlist, EventType.ARR);
    }

    protected void initialize() {
        arrivalProcess.generateNextEvent();
    }

    protected void runEvent(Event e) {
        ServicePoint currentSP;
        Customer a;

        switch (e.getType()) {
            case ARR:
                ServicePoint sp = getShortestQueue();
                sp.addToQueue(new Customer());
                arrivalProcess.generateNextEvent();
                break;
            case DEP:
                currentSP = e.getServicePoint();
                if (currentSP != null) {
                    a = currentSP.removeFromQueue();
                    a.setRemovalTime(Clock.getInstance().getClock());
                    a.reportResults();
                    // Update both queue size and served customers using a single method
                    int spNumber = currentSP.getCashierNumber();
                    controller.updateQueueInfo(spNumber, currentSP.queueLength(), currentSP.getServedCustomers());
                } else {
                    // Handle the case where currentSP is null, log an error
                    System.out.println("Error: ServicePoint is null for DEP event at time " + e.getTime());
                }
                break;
        }

    }

    protected void tryCEvents() {
        for (ServicePoint sp : servicePoints) {
            // C event - conditional
            // if sp ready, and there is a customer in the queue, we start the service.
            if (!sp.isReserved() && sp.isOnQueue()) {
                sp.beginService();
            }
        }
    }

    protected void results() {
        System.out.printf("\nSimulation ended at %.2f\n", Clock.getInstance().getClock());
        System.out.println("Total customers served: " + servicePoints[0].getServedCustomers());
        //System.out.printf("Average service time: %.2f\n", servicePoint[0].getMeanServiceTime());
    }

    private ServicePoint getShortestQueue() {
        ServicePoint shortest = servicePoints[0];

        for (ServicePoint sp : servicePoints) {
            if (sp.queueLength() < shortest.queueLength()) {
                shortest = sp;
            }
        }

        return shortest;
    }
}

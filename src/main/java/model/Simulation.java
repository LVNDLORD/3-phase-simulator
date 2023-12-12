package model;

import controller.Controller;
import db.Dao;
import db.Result;
import model.eduni.distributions.*;

import java.util.ArrayList;

/**
 * The Simulation class represents a queueing system simulation.
 * It simulates a scenario with a specified number of service points (e.g., cashier desks) and
 * customers arriving and being served. The service times and interarrival times are based on
 * specified probability distributions. The simulation runs until a predefined number of customers
 * are served, and calculates metrics like average waiting time.
 * This class extends the Engine class and implements Runnable for execution in a thread.
 */
public class Simulation extends Engine implements Runnable {
    // Actual simulation body
    // Inform controller when simulation's state changes

    private ArrivalProcess arrivalProcess;

    private ServicePoint[] servicePoints;

    private ArrayList<Customer> customers = new ArrayList<>();

    private final Controller controller;
    private int customersServed;

    public enum Distributions {
        Normal,
        Uniform,
        Exponential
    }

    /**
     * Constructs a Simulation instance.
     * Initializes the service points and the arrival process based on specified distributions.
     *
     * @param controller         The controller for the application, used for logging and updating UI.
     * @param servicePointsCount The number of service points (e.g., cashier desks) to simulate.
     * @param dist               The type of probability distribution for service and arrival processes.
     * @param meanSP             The mean for the service point distribution.
     * @param varianceSP         The variance for the service point distribution.
     * @param meanAP             The mean for the arrival process distribution.
     * @param varianceAP         The variance for the arrival process distribution.
     */
    public  Simulation(Controller controller, int servicePointsCount,
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
                // max > min
                // double min, max
                spDist = new Uniform(meanSP, varianceSP); //min, max
                apDist = new Uniform(meanAP, varianceAP); //min, max.
                break;
            case Exponential:
                // mean > 0
                // double mean, long seed
                spDist = new Negexp(meanSP, (long) varianceSP); // mean, seed
                apDist = new Negexp(meanAP, (long) varianceAP); // mean, seed
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

    /**
     * Initializes the simulation by generating the first arrival event.
     */
    protected void initialize() {
        arrivalProcess.generateNextEvent();
    }

    /**
     * Processes a single event in the simulation.
     * Depending on the event type (ARR for arrival, DEP for departure), it handles customer arrivals and departures.
     *
     * @param e The event to be processed in the simulation.
     */
    protected void runEvent(Event e) {
        ServicePoint currentSP;
        Customer a;

        switch (e.getType()) {
            case ARR:
                Customer customer = new Customer();
                ServicePoint sp = getShortestQueue();
                sp.addToQueue(customer);
                customers.add(customer);
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
                    customersServed += 1;
                } else {
                    // Handle the case where currentSP is null, log an error
                    System.out.println("Error: ServicePoint is null for DEP event at time " + e.getTime());
                }
                break;
        }
    }

    /**
     * Tries conditional events (C-events) in the simulation.
     * This method checks each service point and starts service if a service point is ready and there is a customer in the queue.
     */
    protected void tryCEvents() {
        for (ServicePoint sp : servicePoints) {
            // C event - conditional
            // if sp ready, and there is a customer in the queue, we start the service.
            if (!sp.isReserved() && sp.isOnQueue()) {
                sp.beginService();
            }
        }
    }

    /**
     * Gets the service point with the shortest queue.
     * This method is used to determine where a new customer should be added.
     *
     * @return The service point with the shortest queue.
     */
    private ServicePoint getShortestQueue() {
        ServicePoint shortest = servicePoints[0];

        for (ServicePoint sp : servicePoints) {
            if (sp.queueLength() < shortest.queueLength()) {
                shortest = sp;
            }
        }

        return shortest;
    }

    /**
     * Gets the total number of customers served during the simulation.
     *
     * @return The number of customers served.
     */
    public int getCustomersServed() {
        return customersServed;
    }

    /**
     * Ends the simulation, saves the results to a database, and logs the outcome.
     * If the saving process fails, an error message is logged.
     */
    public void end() {
        Dao dao = new Dao();
        Result result = new Result(servicePoints, customers, customersServed, Clock.getInstance().getClock());

        try {
            dao.persist(result);
            System.out.println("Simulation results are saved to database");
        } catch (Exception e) {
            System.out.println("Failed to save simulation results to database. Error: " + e.getMessage());
        }
    }
}

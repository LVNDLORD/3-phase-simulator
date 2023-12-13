package model;

/**
 * Represents a customer in a simulation model, including arrival and removal times.
 */
public class Customer {

    private static final String YELLOW = "\033[0;93m";
    private static final String WHITE = "\033[0;37m";
    private double arrivalTime;
    private double removalTime;
    private static double serviceTimeSum;
    private int id;
    private static int i = 1;

    /**
     * Constructs a new customer with a unique identifier and sets the arrival time.
     */
    public Customer() {
        this.id = i++;
        arrivalTime = Clock.getInstance().getClock();
        System.out.printf("New customer #%d arrived at %.2f\n", id, arrivalTime);
    }

    /**
     * Constructs a new customer with a unique identifier, specified arrival time, and sets the removal time.
     *
     * @param arrivalTime The specified arrival time for the customer.
     */
    public Customer(double arrivalTime) {
        this.id = i++;
        this.arrivalTime = arrivalTime;
        System.out.printf(" New customer #%d arrived at %.2f\n", id, arrivalTime);
    }

    /**
     * Gets the arrival time of the customer.
     *
     * @return The arrival time of the customer.
     */
    public double getArrivalTime() {
        return arrivalTime;
    }

    /**
     * Gets the removal time of the customer.
     *
     * @return The removal time of the customer.
     */
    public double getRemovalTime() {
        return removalTime;
    }

    /**
     * Sets the removal time of the customer.
     *
     * @param removalTime The removal time to set.
     */
    public void setRemovalTime(double removalTime) {
        this.removalTime = removalTime;
    }

    /**
     * Gets the unique identifier of the customer.
     *
     * @return The unique identifier of the customer.
     */
    public int getId() {
        return id;
    }

    /**
     * Reports the results for the customer, including arrival and removal times,
     * duration of stay, and mean service time for all served customers.
     */
    public void reportResults() {
        serviceTimeSum += (removalTime - arrivalTime);
        double meanServiceTime = serviceTimeSum / id;   // id is the number of customers served

        System.out.printf("%sCustomer #%d has been served. model.Customer arrived at:" +
                        " %.2f, removed at: %.2f, stayed for: %.2f. Mean service time: %.2f%s\n",
                YELLOW, id, arrivalTime, removalTime, (removalTime - arrivalTime), meanServiceTime, WHITE);


    }

}

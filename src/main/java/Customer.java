public class Customer {

    private static final String YELLOW = "\033[0;93m";
    private static final String WHITE = "\033[0;37m";
    private double arrivalTime;
    private double removalTime;

    private static double serviceTimeSum;

    private int id;

    private static int i = 1;

    public Customer() {
        this.id = i++;
        arrivalTime = Clock.getInstance().getClock();
        System.out.printf("New customer #%d arrived at %.2f\n", id, arrivalTime);
        //  System.out.println("Customers in queue: " + ServicePoint.getQueueSize());
    }

    public Customer(double arrivalTime) {
        this.id = i++;
        this.arrivalTime = arrivalTime;
        System.out.printf(" New customer #%d arrived at %.2f\n", id, arrivalTime);
    }

    public double getArrivalTime() {
        return arrivalTime;
    }

    public double getRemovalTime() {
        return removalTime;
    }

    public void setRemovalTime(double removalTime) {
        this.removalTime = removalTime;
    }

    public int getId() {
        return id;
    }

    public void reportResults() {
        serviceTimeSum += (removalTime - arrivalTime);
        double meanServiceTime = serviceTimeSum / id;   // id is the number of customers served

        System.out.printf("%sCustomer #%d has been served. Customer arrived at:" +
                        " %.2f, removed at: %.2f, stayed for: %.2f. Mean service time: %.2f%s\n",
                YELLOW, id, arrivalTime, removalTime, (removalTime - arrivalTime), meanServiceTime, WHITE);


    }

}

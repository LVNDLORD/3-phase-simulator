public class Customer {

    private double arrivalTime;
    private double removalTime;

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
        System.out.println("Customer #" + id + " arrived at " + arrivalTime + " mins");
        System.out.println("Customer #" + id + " left at " + removalTime + " mins");
        System.out.println("Customer #" + id + " stayed " + (removalTime - arrivalTime) + " mins in the system\n");
    }

}

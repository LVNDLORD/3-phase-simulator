import eduni.distributions.ContinuousGenerator;
import java.util.LinkedList;

public class ServicePoint {

    private static String GREEN = "\u001B[92m";
    private static String WHITE = "\u001B[37m";
    private LinkedList<Customer> queue;

    private ContinuousGenerator generator;

    private EventList eventlist;

    private EventType eventTypeScheduled;

    private double serviceTimeSum;

    private int servedCustomers;

    private boolean reserved = false;

    public ServicePoint(ContinuousGenerator g, EventList tl, EventType type) {
        this.generator = g;
        this.eventlist = tl;
        this.eventTypeScheduled = type;

        this.serviceTimeSum = 0;
        this.servedCustomers = 0;

        queue = new LinkedList<>();
    }

    public void addToQueue(Customer a) {
        queue.addFirst(a);
    }

    public Customer removeFromQueue() {
        if (!queue.isEmpty()) {
            Customer a = queue.removeLast();
            a.setRemovalTime(Clock.getInstance().getClock());
            double serviceTime = a.getRemovalTime() - a.getArrivalTime();
            System.out.printf("%sCustomer #%d served. Service time: %.2f%s\n", GREEN, a.getId(), serviceTime, WHITE);
            serviceTimeSum += serviceTime;
            servedCustomers++;
            reserved = false; // free the service point
            return a;
        }
        else
            return null;
    }

    public void beginService() {
        assert queue.peek() != null;
        System.out.printf("%sStarting a new service for the customer #%d%s\n", GREEN, queue.peek().getId(), WHITE);
       // System.out.println("Customers in queue: " + (queue.size() - 1));


        reserved = true;
        double serviceTime = generator.sample();
        eventlist.add(new Event(eventTypeScheduled, Clock.getInstance().getClock() + serviceTime));
    }

    public boolean isReserved() {
        return reserved;
    }

    public double getMeanServiceTime() {
        return serviceTimeSum / servedCustomers;
    }

    public boolean isOnQueue() {
        return !queue.isEmpty();
    }

    public int getServedCustomers() {
        return servedCustomers;
    } 

//    public void serve() {
//        Customer a;
//        Normal normalGenerator = new Normal(5, 1);
//
//        a = removeFromQueue();
//        while (a != null) {
//            Clock.getInstance().setClock(Clock.getInstance().getClock() + normalGenerator.sample());
//            // shifting the current time by random (with mean of 5 minutes)
//
//            a.setRemovalTime(Clock.getInstance().getClock());
//            a.reportResults();
//
//            a = removeFromQueue(); // clean the input queue
//        }
//    }

}

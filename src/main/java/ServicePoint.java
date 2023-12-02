import eduni.distributions.ContinuousGenerator;

import java.util.LinkedList;

public class ServicePoint {

    private static String GREEN = "\u001B[92m";
    private static String WHITE = "\u001B[37m";
    private LinkedList<Customer> queue;

    private ContinuousGenerator generator;

    private EventList eventlist;

    private EventType eventTypeScheduled;

    private String name;

    private double serviceTimeSum;

    private int servedCustomers;

    private boolean reserved = false;

    public ServicePoint(String name, ContinuousGenerator g, EventList tl, EventType type) {
        this.generator = g;
        this.eventlist = tl;
        this.eventTypeScheduled = type;
        this.name = name;

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
            servedCustomers++;
            reserved = false; // free the service point
            return a;
        } else
            return null;
    }

    public void beginService() {
        assert queue.peek() != null;
        System.out.printf("%sStarting %s for the customer #%d%s\n", GREEN, name, queue.peek().getId(), WHITE);
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

}

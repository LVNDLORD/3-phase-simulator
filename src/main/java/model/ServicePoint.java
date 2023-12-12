package model;

import model.eduni.distributions.ContinuousGenerator;

import java.util.LinkedList;


/**
 * Represents a service point with a queue of customers and associated operations.
 */
public class ServicePoint {

    private static String GREEN = "\u001B[92m";
    private static String WHITE = "\u001B[37m";
    private LinkedList<Customer> queue;

    private int cashierNumber; // Identifier for this service point

    private ContinuousGenerator generator;

    private EventList eventlist;

    private EventType eventTypeScheduled;

    private String name;

    private double serviceTimeSum;

    private int servedCustomers;

    private boolean reserved = false;

    private QueueUpdateListener queueUpdateListener;

    /**
     * Constructs a ServicePoint with a name, continuous generator, event list, event type,
     * queue update listener, and cashier number.
     *
     * @param name                 The name of the service point.
     * @param g                    The continuous generator for generating service times.
     * @param tl                   The event list associated with the service point.
     * @param type                 The type of events scheduled for this service point.
     * @param listener             The listener for queue updates.
     * @param cashierNumber        The identifier for this service point.
     */
    public ServicePoint(String name, ContinuousGenerator g, EventList tl, EventType type, QueueUpdateListener listener, int cashierNumber) {
        this.generator = g;
        this.eventlist = tl;
        this.eventTypeScheduled = type;
        this.name = name;

        this.serviceTimeSum = 0;
        this.servedCustomers = 0;

        this.queueUpdateListener = listener;
        this.cashierNumber = cashierNumber;

        queue = new LinkedList<>();
    }

    /**
     * Adds a customer to the queue and notifies the queue update listener.
     *
     * @param a The customer to be added to the queue.
     */
    public void addToQueue(Customer a) {
        queue.addFirst(a);
        if (queueUpdateListener != null) {
            queueUpdateListener.updateQueueInfo(cashierNumber, queue.size(), getServedCustomers());
        }
    }

    /**
     * Removes and returns the last customer from the queue, updating served customer count,
     * and notifying the queue update listener.
     *
     * @return The last customer in the queue, or null if the queue is empty.
     */
    public Customer removeFromQueue() {
        if (!queue.isEmpty()) {
            Customer a = queue.removeLast();
            servedCustomers++;
            reserved = false;
            if (queueUpdateListener != null) {
                queueUpdateListener.updateQueueInfo(cashierNumber, queue.size(), servedCustomers);
            }
            return a;
        } else {
            return null;
        }
    }

    /**
     * Initiates service for the next customer in the queue by scheduling a departure event.
     */
    public void beginService() {
        if (!queue.isEmpty()) {
            System.out.printf("%sStarting %s for the customer #%d%s\n", GREEN, name, queue.peek().getId(), WHITE);
            // System.out.println("Customers in queue: " + (queue.size() - 1));

            reserved = true;
            double serviceTime = generator.sample();
            eventlist.add(new Event(eventTypeScheduled, Clock.getInstance().getClock() + serviceTime, this));
        } else {
            System.out.printf("%sNo customer in the queue for %s%s\n", WHITE, name, WHITE);
        }
    }

    /**
     * Checks if the service point is reserved for ongoing service.
     *
     * @return True if reserved, false otherwise.
     */
    public boolean isReserved() {
        return reserved;
    }

    /**
     * Gets the identifier for this service point.
     *
     * @return The cashier number.
     */
    public int getCashierNumber() {
        return cashierNumber;
    }

    /**
     * Calculates and returns the mean service time for this service point.
     *
     * @return The mean service time.
     */
    public double getMeanServiceTime() {
        return serviceTimeSum / servedCustomers;
    }

    /**
     * Checks if the service point has customers in the queue.
     *
     * @return True if there are customers in the queue, false otherwise.
     */
    public boolean isOnQueue() {
        return !queue.isEmpty();
    }

    /**
     * Gets the number of customers served by this service point.
     *
     * @return The number of served customers.
     */
    public int getServedCustomers() {
        return servedCustomers;
    }

    /**
     * Gets the length of the queue.
     *
     * @return The length of the queue.
     */
    public int queueLength() {
        return queue.size();
    }

    /**
     * Gets the name of the service point.
     *
     * @return The name of the service point.
     */
    public String getName() { return name; }
}

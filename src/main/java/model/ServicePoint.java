package model;

import model.eduni.distributions.ContinuousGenerator;

import java.util.LinkedList;

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

    public void addToQueue(Customer a) {
        queue.addFirst(a);
        if (queueUpdateListener != null) {
            queueUpdateListener.updateQueueInfo(cashierNumber, queue.size(), getServedCustomers());
        }
    }

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

    public boolean isReserved() {
        return reserved;
    }

    public int getCashierNumber() {
        return cashierNumber;
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

    public int queueLength() {
        return queue.size();
    }

    public String getName() { return name; }
}

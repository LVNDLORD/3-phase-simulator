// Simulate a queueing system with one service point and one queue.
// The service time is normally distributed with mean 10.
// The interarrival time is exponentially distributed with mean 15.
// The simulation runs until the number of arrivals is 1000.
// The program prints the average waiting time.
// 2:09:10  - 23.11 (how B-phase works)

import eduni.distributions.Negexp;
import eduni.distributions.Normal;

public class MyEngine extends Engine {

    private ArrivalProcess arrivalProcess;

    private ServicePoint[] servicePoint;

    public MyEngine() {
        super();
        servicePoint = new ServicePoint[2]; // array for the service points. Increase the size to add new SPs.
        servicePoint[0] = new ServicePoint("service ONE", new Normal(10, 6), eventlist, EventType.DEP1);
        servicePoint[1] = new ServicePoint("service TWO", new Normal(10, 10), eventlist, EventType.DEP2);
        arrivalProcess = new ArrivalProcess(new Negexp(10), eventlist, EventType.ARR); // average customer arrival time
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

            case DEP1:
                a = servicePoint[0].removeFromQueue(); // removing from the first service point
                servicePoint[1].addToQueue(a); // adding to the second service point
                break;

            case DEP2:
                a = servicePoint[1].removeFromQueue(); // removing customer from the second service point
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
        System.out.println("Total customers served: " + servicePoint[1].getServedCustomers());
        //System.out.printf("Average service time: %.2f\n", servicePoint[0].getMeanServiceTime());
    }
}

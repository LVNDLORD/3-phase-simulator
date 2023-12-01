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
        servicePoint = new ServicePoint[1]; // array of one service point element
        servicePoint[0] = new ServicePoint(new Normal(10, 6), eventlist, EventType.DEP);
        arrivalProcess = new ArrivalProcess(new Negexp(15), eventlist, EventType.ARR);
    }


    protected void initialize() {
        arrivalProcess.generateNextEvent();
    }


    protected void runEvent(Event e) {
        switch (e.getType()) {
            case ARR:
                servicePoint[0].addToQueue(new Customer());
                arrivalProcess.generateNextEvent(); // generate next arrival
                break;

            case DEP:
                servicePoint[0].removeFromQueue();
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
        System.out.println("Total customers served: " + servicePoint[0].getServedCustomers());
        System.out.printf("Average service time: %.2f\n", servicePoint[0].getMeanServiceTime());
    }
}

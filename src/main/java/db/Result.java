package db;

import jakarta.persistence.*;
import model.Customer;
import model.ServicePoint;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Represents the result of a simulation and serves as an ORM entity for database storage.
 */
@Entity
public class Result {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int id;
    private int arrived;
    private int served;
    private double time;
    private int cashiersCount;
    private double avrServiceTime;
    private int avrQueueLength;

    public Result() {}

    /**
     * Constructs a Result object based on simulation outcomes and service point details.
     *
     * @param servicePoints An array of service points involved in the simulation.
     * @param customers     An ArrayList containing the customers participating in the simulation.
     * @param served        The count of customers served during the simulation.
     * @param time          The total time duration of the simulation.
     */
    public Result(ServicePoint[] servicePoints, ArrayList<Customer> customers, int served, double time) {
        this.served = served;
        this.time = time;

        cashiersCount = servicePoints.length;
        arrived = customers.size();

        double totalServiceTime = customers.stream()
                .filter(c -> c.getRemovalTime() > 0)
                .mapToDouble(c -> c.getRemovalTime() - c.getArrivalTime())
                .sum();

        avrServiceTime = totalServiceTime/served;

        int totalInQueue = Arrays.stream(servicePoints)
                .mapToInt(sp -> sp.queueLength())
                .sum();

        avrQueueLength = totalInQueue/cashiersCount;
    }
}

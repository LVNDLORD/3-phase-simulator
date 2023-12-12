package db;

import jakarta.persistence.*;
import model.Customer;
import model.ServicePoint;

import java.util.Arrays;

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

    public Result(ServicePoint[] servicePoints, Customer[] customers, int served, double time) {
        this.served = served;
        this.time = time;

        cashiersCount = servicePoints.length;
        arrived = customers.length;

        double totalServiceTime = Arrays.stream(customers)
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

package model;

/**
 * Functional interface for receiving updates about the queue status in a simulation model.
 */
@FunctionalInterface
public interface QueueUpdateListener {

    /**
     * Notifies the listener about changes in the queue status.
     *
     * @param cashierNumber  The identifier of the service point.
     * @param queueLength    The length of the queue at the service point.
     * @param servedCustomers The number of customers served by the service point.
     */
    void updateQueueInfo(int cashierNumber, int queueLength, int servedCustomers);
}

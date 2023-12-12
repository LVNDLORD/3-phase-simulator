package model;

@FunctionalInterface
public interface QueueUpdateListener {
    void updateQueueInfo(int cashierNumber, int queueLength, int servedCustomers);
}

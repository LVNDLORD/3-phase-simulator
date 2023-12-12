import model.*;
import model.eduni.distributions.Normal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class Tests {

    private ServicePoint servicePoint;
    private EventList eventList;

    @BeforeEach
    void setUp() {
        eventList = new EventList();
        servicePoint = new ServicePoint("TestServicePoint", new Normal(0, 1), eventList, EventType.DEP, null, 1);
    }

    @Test
    void testAddToQueue() {
        Customer customer = new Customer();
        servicePoint.addToQueue(customer);

        assertTrue(servicePoint.isOnQueue());
        assertEquals(1, servicePoint.queueLength());
    }

    @Test
    void testRemoveFromQueue() {
        Customer customer = new Customer();
        servicePoint.addToQueue(customer);

        Customer removedCustomer = servicePoint.removeFromQueue();

        assertNotNull(removedCustomer);
        assertFalse(servicePoint.isOnQueue());
        assertEquals(0, servicePoint.queueLength());
    }

    @Test
    void testBeginService() {
        Customer customer = new Customer();
        servicePoint.addToQueue(customer);

        assertFalse(servicePoint.isReserved());

        servicePoint.beginService();

        assertTrue(servicePoint.isReserved());
    }

    @Test
    void testGetCashierNumber() {
        assertEquals(1, servicePoint.getCashierNumber());
    }

    @Test
    void testQueueLengthAfterService() {
        // Add customers and simulate service
        for (int i = 1; i <= 3; i++) {
            Customer customer = new Customer();
            servicePoint.addToQueue(customer);
            servicePoint.beginService();
            servicePoint.removeFromQueue(); // Remove the customer after service
            System.out.println("Queue length after service: " + servicePoint.queueLength());
        }

        assertEquals(0, servicePoint.queueLength());
    }

    @Test
    void testMultipleServicePoints() {
        // Create multiple service points
        ServicePoint[] servicePoints = new ServicePoint[3];
        for (int i = 0; i < 3; i++) {
            servicePoints[i] = new ServicePoint("ServicePoint" + i, new Normal(0, 1), eventList, EventType.DEP, null, i);
        }

        // Add customers to each service point
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 5; j++) {
                servicePoints[i].addToQueue(new Customer());
            }
        }

        // Simulate service
        for (int i = 0; i < 3; i++) {
            servicePoints[i].beginService();
            Customer servicedCustomer = servicePoints[i].removeFromQueue(); // Remove the customer after service
            System.out.println("Serviced customer for ServicePoint" + i + ": " + servicedCustomer);
            System.out.println("Queue length after service for ServicePoint" + i + ": " + servicePoints[i].queueLength());
        }

        // Check if the queue lengths are as expected
        assertEquals(4, servicePoints[0].queueLength(), "Queue length not zero for ServicePoint0");
        assertEquals(4, servicePoints[1].queueLength(), "Queue length not zero for ServicePoint1");
        assertEquals(4, servicePoints[2].queueLength(), "Queue length not zero for ServicePoint2");
    }

    @Test
    void testAddToQueueMultipleCustomers() {
        // Add multiple customers to the queue
        for (int i = 1; i <= 5; i++) {
            Customer customer = new Customer();
            servicePoint.addToQueue(customer);
            assertTrue(servicePoint.isOnQueue());
            assertEquals(i, servicePoint.queueLength());
        }
    }

    @Test
    void testRemoveFromEmptyQueue() {
        // Try to remove from an empty queue
        assertNull(servicePoint.removeFromQueue());
        assertFalse(servicePoint.isOnQueue());
        assertEquals(0, servicePoint.queueLength());
    }

    @Test
    void testBeginServiceWithEmptyQueue() {
        // Ensure the queue is initially empty
        assertEquals(0, servicePoint.queueLength());

        // Begin service with an empty queue
        servicePoint.beginService();

        // Assert that the service point is not reserved and the queue length remains zero
        assertFalse(servicePoint.isReserved());
        assertEquals(0, servicePoint.queueLength());
    }

    @Test
    void testBeginServiceMultipleCustomers() {
        // Add customers to the queue and begin service
        for (int i = 1; i <= 3; i++) {
            Customer customer = new Customer();
            servicePoint.addToQueue(customer);
            servicePoint.beginService();
            assertTrue(servicePoint.isReserved());
        }
    }

    @Test
    void testServiceAndRemoveCustomer() {
        // Add a customer, begin service, and remove the customer after service
        Customer customer = new Customer();
        servicePoint.addToQueue(customer);
        servicePoint.beginService();
        Customer servicedCustomer = servicePoint.removeFromQueue();

        assertNotNull(servicedCustomer);
        assertFalse(servicePoint.isReserved());
        assertEquals(0, servicePoint.queueLength());
    }
    // Add more tests as needed for other methods and edge cases
}
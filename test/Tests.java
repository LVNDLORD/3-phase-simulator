import model.*;
import model.eduni.distributions.Normal;
import model.eduni.distributions.Uniform;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class Tests {

    private ServicePoint servicePoint;
    private EventList eventList;

    @BeforeEach
    void setUp() {
        eventList = new EventList();
        servicePoint = new ServicePoint("TestServicePoint", new Normal(0, 1), eventList, EventType.DEP, null, 1);
    }

    @Nested
    class QueueOperations {

        // Test adding a customer to the queue
        @Test
        void testAddToQueue() {
            Customer customer = new Customer();
            servicePoint.addToQueue(customer);

            assertTrue(servicePoint.isOnQueue());
            assertEquals(1, servicePoint.queueLength());
        }

        // Test removing a customer from the queue
        @Test
        void testRemoveFromQueue() {
            Customer customer = new Customer();
            servicePoint.addToQueue(customer);

            Customer removedCustomer = servicePoint.removeFromQueue();

            assertNotNull(removedCustomer);
            assertFalse(servicePoint.isOnQueue());
            assertEquals(0, servicePoint.queueLength());
        }

        // Test adding multiple customers to the queue
        @Test
        void testAddMultipleToQueue() {
            for (int i = 1; i <= 5; i++) {
                Customer customer = new Customer();
                servicePoint.addToQueue(customer);
            }

            assertTrue(servicePoint.isOnQueue());
            assertEquals(5, servicePoint.queueLength());
        }

        // Test removing multiple customers from the queue
        @Test
        void testRemoveMultipleFromQueue() {
            // Add customers to the queue
            for (int i = 1; i <= 3; i++) {
                Customer customer = new Customer();
                servicePoint.addToQueue(customer);
            }

            // Remove customers from the queue
            for (int i = 1; i <= 2; i++) {
                Customer removedCustomer = servicePoint.removeFromQueue();
                assertNotNull(removedCustomer);
            }

            assertTrue(servicePoint.isOnQueue());
            assertEquals(1, servicePoint.queueLength());
        }
    }

    @Nested
    class ServiceOperations {

        // Test beginning service for a customer in the queue
        @Test
        void testBeginService() {
            Customer customer = new Customer();
            servicePoint.addToQueue(customer);

            assertFalse(servicePoint.isReserved());

            servicePoint.beginService();

            assertTrue(servicePoint.isReserved());
        }

        // Test queue length after serving multiple customers
        @Test
        void testQueueLengthAfterService() {
            for (int i = 1; i <= 3; i++) {
                Customer customer = new Customer();
                servicePoint.addToQueue(customer);
                servicePoint.beginService();
                servicePoint.removeFromQueue();
            }

            assertEquals(0, servicePoint.queueLength());
        }

        // Add more service-related tests...
    }

    @Nested
    class MultipleServicePoints {

        // Test multiple service points and their queue lengths
        @Test
        void testMultipleServicePoints() {
            ServicePoint[] servicePoints = new ServicePoint[3];
            for (int i = 0; i < 3; i++) {
                servicePoints[i] = new ServicePoint("ServicePoint" + i, new Normal(0, 1), eventList, EventType.DEP, null, i);
            }

            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 5; j++) {
                    servicePoints[i].addToQueue(new Customer());
                }
            }

            for (int i = 0; i < 3; i++) {
                servicePoints[i].beginService();
                Customer servicedCustomer = servicePoints[i].removeFromQueue();
                System.out.println("Serviced customer for ServicePoint" + i + ": " + servicedCustomer);
                System.out.println("Queue length after service for ServicePoint" + i + ": " + servicePoints[i].queueLength());
            }

            assertEquals(4, servicePoints[0].queueLength(), "Queue length not zero for ServicePoint0");
            assertEquals(4, servicePoints[1].queueLength(), "Queue length not zero for ServicePoint1");
            assertEquals(4, servicePoints[2].queueLength(), "Queue length not zero for ServicePoint2");
        }

        // Add more tests for multiple service points...
    }

    // Add more nested classes and tests...

    @Nested
    class DistributionTests {

        // Test normal distribution characteristics
        @Test
        void testNormalDistribution() {
            Normal normalDistribution = new Normal(0, 1);

            int sampleSize = 1000;
            double[] samples = new double[sampleSize];
            for (int i = 0; i < sampleSize; i++) {
                samples[i] = normalDistribution.sample();
            }

            double mean = calculateMean(samples);
            double tolerance = 0.1;
            assertTrue(Math.abs(mean - 0) < tolerance, "Mean is not close to 0");

            double stdDev = calculateStandardDeviation(samples);
            assertTrue(Math.abs(stdDev - 1) < tolerance, "Standard deviation is not close to 1");
        }

        // Test uniform distribution characteristics
        @Test
        void testUniformDistribution() {
            // Create a uniform distribution with min=0 and max=1
            Uniform uniformDistribution = new Uniform(0, 1);

            // Perform statistical tests or assertions
            int sampleSize = 1000;
            double[] samples = new double[sampleSize];
            for (int i = 0; i < sampleSize; i++) {
                samples[i] = uniformDistribution.sample();
            }

            // Example: Check if the mean is close to (min + max) / 2
            double expectedMean = (0 + 1) / 2.0;
            double tolerance = 0.1; // Adjust as needed
            double mean = calculateMean(samples);
            assertTrue(Math.abs(mean - expectedMean) < tolerance, "Mean is not close to the expected value");

            // Example: Check if the standard deviation is close to sqrt((max - min)^2 / 12)
            double expectedStdDev = Math.sqrt(Math.pow((1 - 0), 2) / 12.0);
            double stdDev = calculateStandardDeviation(samples);
            assertTrue(Math.abs(stdDev - expectedStdDev) < tolerance, "Standard deviation is not close to the expected value");
        }

        // Test exponential distribution characteristics
        @Test
        void testExponentialDistribution() {
            // Parameters for the exponential distribution
            double mean = 1.0; // You can set your desired mean

            // Perform statistical tests or assertions
            int sampleSize = 1000;
            double[] samples = new double[sampleSize];
            for (int i = 0; i < sampleSize; i++) {
                // Use the inverse transform method for Exponential distribution
                samples[i] = -mean * Math.log(Math.random());
            }

            // Example: Check if the mean is close to the expected mean
            double tolerance = 0.1; // Adjust as needed
            double calculatedMean = calculateMean(samples);
            assertTrue(Math.abs(calculatedMean - mean) < tolerance, "Mean is not close to the expected value");

            // Example: Check if the standard deviation is close to the expected standard deviation
            double expectedStdDev = mean;
            double calculatedStdDev = calculateStandardDeviation(samples);
            assertTrue(Math.abs(calculatedStdDev - expectedStdDev) < tolerance, "Standard deviation is not close to the expected value");
        }
    }

    // Test service point initialization
    @Test
    void testServicePointInitialization() {
        assertEquals(0, servicePoint.queueLength());
        assertFalse(servicePoint.isReserved());
    }

    // Helper methods below...

    private double calculateMean(double[] values) {
        double sum = 0;
        for (double value : values) {
            sum += value;
        }
        return sum / values.length;
    }

    private double calculateStandardDeviation(double[] values) {
        double mean = calculateMean(values);
        double sumSquaredDiff = 0;
        for (double value : values) {
            double diff = value - mean;
            sumSquaredDiff += diff * diff;
        }
        return Math.sqrt(sumSquaredDiff / values.length);
    }
}
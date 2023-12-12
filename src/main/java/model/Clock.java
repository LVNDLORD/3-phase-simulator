package model;

/**
 * Singleton class representing a clock for time synchronization during the application runtime.
 */
public class Clock {

    private static Clock instance;

    private double clock;

    /**
     * Private constructor to prevent instantiation from outside the class.
     */
    private Clock() {

    }

    /**
     * Gets the singleton instance of the Clock class.
     *
     * @return The singleton instance of the Clock class.
     */
    public static Clock getInstance() {
        if (instance == null) {
            instance = new Clock();
        }
        return instance;

    }

    /**
     * Gets the current value of the simulation clock.
     *
     * @return The current value of the simulation clock.
     */
    public double getClock() {
        return clock;
    }

    /**
     * Sets the value of the simulation clock.
     *
     * @param clock The new value to set for the simulation clock.
     */
    public void setClock(double clock) {
        this.clock = clock;
    }
}

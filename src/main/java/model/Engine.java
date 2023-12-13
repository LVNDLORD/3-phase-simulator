package model;

/**
 * An abstract class representing the core engine for simulation models.
 */
public abstract class Engine {
    private static final String RED = "\033[0;31m";
    private static final String WHITE = "\033[0;37m";
    private double simulationTime = 0;

    protected EventList eventlist;

    /**
     * Constructs an Engine with an initialized event list.
     */
    public Engine() {
        eventlist = new EventList();
        // Service Points are created in the subclass
    }

    /**
     * Sets the simulation time for the system.
     *
     * @param simulationTime The simulation time to set.
     */
    public void setSimulationTime(double simulationTime) {
        this.simulationTime = simulationTime;
    }


    /**
     * General implementation of the 3-phase simulation
     * Runs the 3-phase simulation: A-phase, B-phase, and C-phase.
     */
    public void run() {
        initialize();

        while (simulate()) {
            System.out.printf("\n%sA-phase:\n%sTime is %.2f\n", RED, WHITE, currentTime());
            Clock.getInstance().setClock(currentTime());

            System.out.printf("%sB-phase:%s \n", RED, WHITE);
            runBEvents();

            System.out.printf("%sC-phase:%s \n", RED, WHITE);
            tryCEvents(); // put the customer to the service point queue

        }

        end();
    }

    /**
     * Checks if the simulation should continue based on the current time.
     *
     * @return True if the simulation should continue, false otherwise.
     */
    private boolean simulate() {
        return Clock.getInstance().getClock() < simulationTime;
    }

    /**
     * Gets the current simulation time from the event list.
     *
     * @return The current simulation time.
     */
    private double currentTime() {
        return eventlist.getNextEventTime();
    }

    /**
     * Runs events scheduled for the B-phase.
     */
    private void runBEvents() {
        while (eventlist.getNextEventTime() == Clock.getInstance().getClock()) {
            runEvent(eventlist.remove());
        }
    }

    /**
     * Abstract method for initializing the simulation.
     */
    protected abstract void initialize();

    /**
     * Abstract method for running a specific event.
     *
     * @param e The event to be processed.
     */
    protected abstract void runEvent(Event e);

    /**
     * Abstract method for handling events in the C-phase,
     * Putting customers into service point queues.
     * Optional as executes only if any customers are in the queue
     */
    protected abstract void tryCEvents();

    /**
     * Abstract method for finalizing the simulation.
     */
    protected abstract void end();
}

public abstract class Engine {
    private static final String RED = "\033[0;31m"; // https://gist.github.com/fnky/458719343aabd01cfb17a3a4f7296797
    private static final String WHITE = "\033[0;37m"; // ANSI escape code for white color
    private double simulationTime = 0;

    protected EventList eventlist;

    public Engine() {
        eventlist = new EventList();
        // Service Points are created in the subclass
    }

    // simulation time of the system
    public void setSimulationTime(double simulationTime) {
        this.simulationTime = simulationTime;
    }

    // general implementation of the 3-phase simulation
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
        results();
    }


    private boolean simulate() {
        return Clock.getInstance().getClock() < simulationTime;
    }

    private double currentTime() {
        return eventlist.getNextEventTime();
    }

    private void runBEvents() {
        while (eventlist.getNextEventTime() == Clock.getInstance().getClock()) {
            runEvent(eventlist.remove());
        }
    }

    protected abstract void initialize();

    protected abstract void runEvent(Event e);

    protected abstract void tryCEvents();

    protected abstract void results();

}

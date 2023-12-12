package model;// to add arrival events to the event list

import model.eduni.distributions.ContinuousGenerator;
import model.eduni.distributions.Negexp;

/**
 * Represents a process generating arrival events based on a continuous generator.
 */
public class ArrivalProcess {
    private ContinuousGenerator generator;
    private EventList eventlist;
    private EventType type;

    /**
     * Constructs an ArrivalProcess with a continuous generator, event list, and event type.
     *
     * @param g    The continuous generator for generating arrival times.
     * @param tl   The event list to which arrival events will be added.
     * @param type The type of arrival events (ARR).
     */
    public ArrivalProcess(ContinuousGenerator g, EventList tl, EventType type){
        this.generator = g;
        this.eventlist = tl;
        this.type = type;
    }

    /**
     * Generates the next arrival event and adds it to the event list.
     *
     * @return The time of the generated arrival event.
     */
    public double generateNextEvent() {
        double eventTime = Clock.getInstance().getClock() + generator.sample();
        Event t = new Event(type, eventTime);
        eventlist.add(t);

        return eventTime;
    }

    /**
     * Main method for testing the ArrivalProcess class.
     *
     * @param args Command-line arguments (not used in this context).
     */
    public static void main(String[] args) {
        EventList eventlist = new EventList();
        ArrivalProcess arrivalProcess = new ArrivalProcess(new Negexp(10), eventlist, EventType.ARR );
        // new Negexp( mean value time between arrivals, seed (first init) value)

        for (int i = 0; i < 10; i++) {
            Clock.getInstance().setClock(arrivalProcess.generateNextEvent());  // 35:00 16.11

        }
        eventlist.print();
    }

}

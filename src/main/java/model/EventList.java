package model;

import java.util.Arrays;
import java.util.PriorityQueue;

/**
 * Represents a list of events using a PriorityQueue.
 */
public class EventList {
    private PriorityQueue<Event> eventList;

    /**
     * Constructs an empty EventList.
     */
    public EventList() {
        eventList = new PriorityQueue<>();
    }

    /**
     * Adds an event to the event list.
     *
     * @param e The event to be added.
     */
    public void add(Event e) {
        // create a new arrival event for the next customer who will arrive at e.getTime() OR
        //creating a departure event for the customer who is currently being served, and will be ready at e.getTime()
        System.out.printf("Adding to the event list %s %.2f\n", e.getType(), e.getTime());
        eventList.add(e);
    }

    /**
     * Removes and returns the next event from the event list.
     *
     * @return The next event, or null if the event list is empty.
     */
    public Event remove() {
        if (eventList.isEmpty())
            return null;
        // remove the event from the list
        System.out.printf("Removing from the event list %s %.2f\n", eventList.peek().getType(), eventList.peek().getTime());
        return eventList.remove();
    }

    /**
     * Gets the time of the next event without removing it.
     *
     * @return The time of the next event, or 0 if the event list is empty.
     */
    public double getNextEventTime() {
        if (eventList.isEmpty())
            return 0;
        return eventList.peek().getTime();
    }

    /**
     * Prints the events in the event list.
     */
    public void print() {
        Object[] tmp = eventList.toArray();
        Arrays.sort(tmp);
        for (Object e : tmp) {
            System.out.println(e);
        }
    }
}

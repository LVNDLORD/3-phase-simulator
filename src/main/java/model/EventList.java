package model;

import java.util.Arrays;
import java.util.PriorityQueue;

public class EventList {
    private PriorityQueue<Event> eventList;

    public EventList() {
        eventList = new PriorityQueue<>();
    }

    public void add(Event e) {
        // create a new arrival event for the next customer who will arrive at e.getTime() OR
        //creating a departure event for the customer who is currently being served, and will be ready at e.getTime()
        System.out.printf("Adding to the event list %s %.2f\n", e.getType(), e.getTime());
        eventList.add(e);
    }

    public Event remove() {
        if (eventList.isEmpty())
            return null;
        // remove the event from the list
        System.out.printf("Removing from the event list %s %.2f\n", eventList.peek().getType(), eventList.peek().getTime());
        return eventList.remove();
    }

    public double getNextEventTime() {
        if (eventList.isEmpty())
            return 0;
        return eventList.peek().getTime();
    }

    public void print() {
        Object[] tmp = eventList.toArray();
        Arrays.sort(tmp);
        for (Object e : tmp) {
            System.out.println(e);
        }
    }
}

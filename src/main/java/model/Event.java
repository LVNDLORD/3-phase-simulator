package model;

/**
 * Represents an event with a type, time, and optional service point information.
 */
public class Event implements Comparable<Event> {

    private EventType type;
    private double time;
    private ServicePoint sp;

    /**
     * Constructs an event with a type and time.
     *
     * @param type The type of the event (ARR or DEP).
     * @param time The time of the event.
     */
    public Event(EventType type, double time) {
        this.type = type;
        this.time = time;
    }

    /**
     * Constructs a departure event with a type, time, and associated service point.
     *
     * @param type The type of the event (DEP).
     * @param time The time of the event.
     * @param sp   The service point associated with the departure event.
     * @throws IllegalArgumentException if the service point is null for departure events.
     */
    public Event(EventType type, double time, ServicePoint sp) {
        this.type = type;
        this.time = time;

        // If event type - departure, record from which service point customer should departure
        if (type == EventType.DEP) {
            if (sp == null) {
                throw new IllegalArgumentException("ServicePoint cannot be null for DEP events");
            }
            this.sp = sp;
        }
    }

    /**
     * Gets the type of the event.
     *
     * @return The type of the event.
     */
    public EventType getType() {
        return type;
    }

    /**
     * Gets the time of the event.
     *
     * @return The time of the event.
     */
    public double getTime() {
        return time;
    }

    /**
     * Gets the service point associated with the departure event.
     *
     * @return The service point associated with the departure event, or null for arrival events.
     */
    public ServicePoint getServicePoint() {
        return sp;
    }

    /**
     * Provides a string representation of the event.
     *
     * @return A string containing the time and type of the event.
     */
    @Override
    public String toString() {
        return time + " [" + type + "]";
    }

    // used in priorityQueue as a parameter to order according to
    /**
     * Compares two events based on their times.
     *
     * @param e The event to compare to.
     * @return -1 if this event is earlier, 1 if later, and 0 if equal.
     */
    @Override
    public int compareTo(Event e) { //Comparable requires compareTo method
        if (time < e.time) {
            return -1;
        } else if (time > e.time) {
            return 1;
        }
        return 0;
    }
}

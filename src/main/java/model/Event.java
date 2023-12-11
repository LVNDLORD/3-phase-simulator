package model;

public class Event implements Comparable<Event> {

    private EventType type;
    private double time;
    private ServicePoint sp;


    public Event(EventType type, double time) {
        this.type = type;
        this.time = time;
    }

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

    public EventType getType() {
        return type;
    }

    public double getTime() {
        return time;
    }


    @Override
    public String toString() {
        return time + " [" + type + "]";
    }

    // used in priorityQueue as a parameter to order according to
    @Override
    public int compareTo(Event e) { //Comparable requires compareTo method
        if (time < e.time) {
            return -1;
        } else if (time > e.time) {
            return 1;
        }
        return 0;
    }

    public ServicePoint getServicePoint() {
        return sp;
    }
}

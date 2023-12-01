public class Event implements Comparable<Event> {

    private EventType type;
    private double time;


    public Event(EventType type, double time) {
        this.type = type;
        this.time = time;
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
}

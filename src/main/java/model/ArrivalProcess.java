package model;// to add arrival events to the event list

import model.distributions.ContinuousGenerator;
import model.distributions.Negexp;

public class ArrivalProcess {
    private ContinuousGenerator generator;
    private EventList eventlist;
    private EventType type;
    public ArrivalProcess(ContinuousGenerator g, EventList tl, EventType type){
        this.generator = g;
        this.eventlist = tl;
        this.type = type;
    }

    public double generateNextEvent() {
        double eventTime = Clock.getInstance().getClock() + generator.sample();
        Event t = new Event(type, eventTime);
        eventlist.add(t);

        return eventTime;
    }

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

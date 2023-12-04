package controller;

import model.Simulation;

public class Controller {
    // When user interacts with interface - update simulation state
    // When simulation's state changes - update interface

    private Simulation sim = new Simulation(this);

    public Controller() {
        log("Controller initialized");
    }

    private void log (String s) {
        System.out.println(s);
    }
}

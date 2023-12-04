package controller;

import model.Simulation;

public class Controller {
    // When user interacts with interface - update simulation state
    // When simulation's state changes - update interface

    private Simulation sim;

    public Controller() {
        log("Controller initialized");
    }

    /**
     *
     * @param servicePointsCount Number of initial service points
     * @param customersCount Total number of customers to be served
     * @return Boolean value, indicating whether simulation launch succeeded or failed
     */
    public boolean startSimulation(int servicePointsCount, int customersCount) {
        try {
            sim = new Simulation(this);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private void log (String s) {
        System.out.println(s);
    }
}

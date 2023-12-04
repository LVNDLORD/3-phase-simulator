package controller;

import java.io.InputStream;

import javafx.fxml.FXML;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import model.Simulation;

public class Controller {
    // When user interacts with interface - update simulation state
    // When simulation's state changes - update interface
    @FXML
    private Slider cashierSlider;
    @FXML
    private HBox cashierContainer; // The container for cashier images

    private Simulation sim;

    public Controller() {
        log("Controller initialized");
    }

    public void initialize() {
        cashierSlider.valueProperty().addListener((obs, oldValue, newValue) -> {
            updateCashierDesks(newValue.intValue());
        });
    }

    private void updateCashierDesks(int count) {
        cashierContainer.getChildren().clear(); // Clear existing cashiers

        for (int i = 0; i < count; i++) {
            try {
                InputStream is = getClass().getResourceAsStream("/cashier.png");
                if (is == null) {
                    log("InputStream is null - Image not found.");
                    continue; // Skip this iteration if the image is not found
                }

                Image cashierImage = new Image(is);
                ImageView imageView = new ImageView(cashierImage);
                imageView.setFitHeight(100);
                imageView.setFitWidth(50);
                imageView.setPreserveRatio(true);
                cashierContainer.getChildren().add(imageView);

                log("Cashier image added.");
            } catch (Exception e) {
                log("Exception occurred: " + e.getMessage());
            }
        }
    }

    /**
     * Attempts to launch simulation (model) with given parameters.
     * 
     * @param servicePointsCount Number of initial service points
     * @param customersCount     Total number of customers to be served
     * @param simulationTime     For how long simulation should run (in simulation time, not real time)
     * @param distribution       Distribution type. Possible values are "Normal", "Binomial", "Exponential" and "Poisson"
     * @return Boolean value, indicating whether simulation launch succeeded or
     *         failed
     */
    public boolean startSimulation(int servicePointsCount, int customersCount, int simulationTime, Simulation.Distributions distribution) {
        try {
            sim = new Simulation(this);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private void log(String s) {
        System.out.println(s);
    }
}

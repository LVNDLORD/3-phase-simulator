package controller;

import java.io.InputStream;

import javafx.fxml.FXML;
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import model.Simulation;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

public class Controller {
    @FXML
    private Slider cashierSlider;
    @FXML
    private HBox cashierContainer; // The container for cashier images
    // Reference to the log TextArea in the "Logs" section
    @FXML
    private TextArea logTextArea;
    @FXML
    private Accordion accordion;
    private static final String RED = "\033[0;31m";

    private Simulation sim;

    public Controller() {
        log("Controller initialized");
    }

    public void initialize() {
        cashierSlider.valueProperty().addListener((obs, oldValue, newValue) -> {
            updateCashierDesks(newValue.intValue());
        });
        // Set the "Logs" TitledPane to be expanded by default
        accordion.setExpandedPane(accordion.getPanes().get(0));
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
            } catch (Exception e) {
                log("Exception occurred: " + e.getMessage(), RED);
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
        if (servicePointsCount < 1) {
            log("Number of service points should be positive", RED);
            return false;
        }

        if (simulationTime < 1) {
            log("Simulation time should be positive", RED);
            return false;
        }

        try {
            sim = new Simulation(this);
            sim.setSimulationTime(simulationTime);
            sim.run();
            return true;
        } catch (Exception e) {
            log("Failed to launch simulation. Error: " + e.getMessage(), RED);
            return false;
        }
    }

    @FXML
    public void start(MouseEvent mouseEvent) {
        int cashiersCount = (int)Math.floor(cashierSlider.getValue());
        startSimulation(cashiersCount, 100, 50, Simulation.Distributions.Normal);
    }

    private void log(Object s) {
        s = s.toString();

        // Get the current time in HH:mm:ss format
        LocalTime currentTime = LocalTime.parse(LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")));

        System.out.println(currentTime + " " + s);

        if (logTextArea != null) {
            logTextArea.appendText(currentTime + " " + s + "\n");
        }
    }

    private void log(Object s, String color) {
        s = s.toString();

        // Get the current time in HH:mm:ss format
        LocalTime currentTime = LocalTime.parse(LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")));

        System.out.printf("%s" + currentTime + " " + s + "%s \n", color, "\033[0;38m");

        if (logTextArea != null) {
            logTextArea.appendText(currentTime + " " + s + "\n");
        }
    }
}
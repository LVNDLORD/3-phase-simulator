package controller;

import java.io.InputStream;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import model.Simulation;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

public class Controller {


    @FXML
    private Slider cashierSlider;
    @FXML
    private HBox cashierContainer; // The container for cashier images
    // Reference to the log TextArea in the "Logs" section
    @FXML
    private Slider numOfCustomersSlider;
    @FXML
    private TextArea logTextArea;
    @FXML
    private ComboBox<String> distributionComboBox;
    @FXML
    private ComboBox<String> simulationTimeComboBox;
    @FXML
    private TextField meanValueTextField;
    @FXML
    private TextField varianceValueTextField;
    @FXML
    private Label varianceLabel;
    @FXML
    private Label meanLabel;

    private String selectedDistribution = "Normal"; // Default distribution type

    @FXML
    private TextFlow logTextFlow;
    @FXML
    private ScrollPane logScrollPane;
    @FXML
    private Accordion accordion;
    public static final String RED = "\033[0;31m";

    private Simulation sim;

    public Controller() {
        log("Controller initialized");
    }

    public void initialize() {
        cashierSlider.valueProperty().addListener((obs, oldValue, newValue) -> {
            updateCashierDesks(newValue.intValue());
        });

        // Initialize the distributionComboBox
        distributionComboBox.getItems().addAll("Normal", "Uniform", "Exponential");
        distributionComboBox.setValue(selectedDistribution); // Set default selection

        distributionComboBox.valueProperty().addListener((obs, oldValue, newValue) -> {
            updateLabelsWithDistribution(newValue);
        });

        // Initialize the simulationTimeComboBox
        for (int hour = 1; hour <= 12; hour++) {
            simulationTimeComboBox.getItems().add(String.valueOf(hour));
        }
        simulationTimeComboBox.setValue("1"); // Set default selection


        // Set the "Logs" TitledPane to be expanded by default
        accordion.setExpandedPane(accordion.getPanes().get(0));
        // Bind the vvalue property of the ScrollPane to the height of the TextFlow
        logScrollPane.vvalueProperty().bind(logTextFlow.heightProperty());
    }

    private void updateLabelsWithDistribution(String selectedDistribution) {
        switch (selectedDistribution) {
            case "Uniform":
                // If the selected distribution is "Uniform", update the labels
                meanLabel.setText("Min");
                varianceLabel.setText("Max");
                break;
            case "Exponential":
                // If the selected distribution is "Uniform", update the labels
                meanLabel.setText("Min");
                varianceLabel.setText("Seed");
                break;
            default: // Normal
                // For other distributions, set the default label text
                meanLabel.setText("Mean");
                varianceLabel.setText("Variance");
                break;
        }
    }

    private void updateCashierDesks(int count) {
        cashierContainer.getChildren().clear(); // Clear existing cashiers

        for (int i = 0; i < count; i++) {
            try {
                InputStream is = getClass().getResourceAsStream("/cashier.png");
                if (is == null) {
                    log("InputStream is null - Image not found.", RED);
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
     * @param simulationTime     For how long simulation should run (in simulation
     *                           time, not real time)
     * @param distribution       Distribution type. Possible values are "Normal",
     *                           "Binomial", "Exponential" and "Poisson"
     * @return Boolean value, indicating whether simulation launch succeeded or
     *         failed
     */
    public boolean startSimulation(int servicePointsCount, int customersCount, int simulationTime,
            Simulation.Distributions distribution, double mean, double variance) {
        if (servicePointsCount < 1) {
            log("Number of service points should be positive", RED);
            return false;
        }

        if (simulationTime < 1) {
            log("Simulation time should be positive", RED);
            return false;
        }

        try {
            sim = new Simulation(this, servicePointsCount, customersCount, distribution, mean, variance);
            sim.setSimulationTime(simulationTime*60);
            sim.run();
            return true;
        } catch (Exception e) {
            log("Failed to launch simulation. Error: " + e.getMessage(), RED);
            return false;
        }
    }

    /**
     * Runs when user clicks start button. Fetches input values and passes them to <em>startSimulation</em>
     */
    @FXML
    public void start(MouseEvent mouseEvent) {
        int cashiersCount = (int) Math.floor(cashierSlider.getValue());

        Simulation.Distributions distribution;
        String distName = distributionComboBox.getValue();

        switch(distName) {
            case "Normal":
                distribution = Simulation.Distributions.Normal;
                break;
            case "Uniform":
                distribution = Simulation.Distributions.Uniform;

                break;
            case "Exponential":
                distribution = Simulation.Distributions.Exponential;
                break;
            default:
                log("Unknown distribution: " + distName, RED);
                return;
        }

        int time;
        double mean, variance;
        try {
            time = Integer.parseInt(simulationTimeComboBox.getValue());
            mean = Double.parseDouble(meanValueTextField.getText());
            variance = Double.parseDouble(varianceValueTextField.getText());
        } catch (Exception e) {
            log("Time cannot be cast to integer " + simulationTimeComboBox.getValue());
            return;
        }

        int customersCount = (int) Math.floor(numOfCustomersSlider.getValue());

        startSimulation(cashiersCount, customersCount, time, distribution, mean, variance);
    }

    public void log(Object s) {
        s = s.toString();

        // Get the current time in HH:mm:ss format
        LocalTime currentTime = LocalTime.parse(LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")));

        System.out.println(currentTime + " " + s);

        if (logTextFlow != null) {
            Text text = new Text(currentTime + " " + s + "\n");
            logTextFlow.getChildren().add(text);
        }
    }

    public void log(Object s, String color) {
        s = s.toString();

        // Get the current time in HH:mm:ss format
        LocalTime currentTime = LocalTime.parse(LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")));

        System.out.printf("%s" + currentTime + " " + s + "%s \n", color, "\033[0;38m");

        if (logTextFlow != null) {
            Text text = new Text(currentTime + " " + s + "\n");
            switch (color.toLowerCase()) {
                case RED -> text.setFill(Color.RED);
                //case GREEN -> text.setFill(Color.GREEN); -> Example if we need more colors, add them as class constants then here.
                default -> text.setFill(Color.BLACK); // Default to black for unknown colors - Add them as needed above
            }

            logTextFlow.getChildren().add(text);
        }
    }
}
package controller;

import java.io.InputStream;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import model.Simulation;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import view.*;

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
    private TextField meanSPDistValueTextField;
    @FXML
    private TextField varianceSPDistValueTextField;
    @FXML
    private TextField meanAPDistValueTextField;
    @FXML
    private TextField varianceAPDistValueTextField;
    @FXML
    private Label SPvarianceLabel;
    @FXML
    private Label SPmeanLabel;
    @FXML
    private Label APvarianceLabel;
    @FXML
    private Label APmeanLabel;
    @FXML
    private Button helpButton;

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
        helpButton.setOnAction(event -> openInfo());
    }

    private void updateLabelsWithDistribution(String selectedDistribution) {
        switch (selectedDistribution) {
            case "Uniform":
                // If the selected distribution is "Uniform", update the labels
                SPmeanLabel.setText("Min");
                SPvarianceLabel.setText("Max");
                APmeanLabel.setText("Min");
                APvarianceLabel.setText("Max");
                break;
            case "Exponential":
                // If the selected distribution is "Uniform", update the labels
                SPmeanLabel.setText("Mean");
                SPvarianceLabel.setText("Seed");
                APmeanLabel.setText("Mean");
                APvarianceLabel.setText("Seed");
                break;
            default: // Normal
                // For other distributions, set the default label text
                SPmeanLabel.setText("Mean");
                SPvarianceLabel.setText("Variance");
                APmeanLabel.setText("Mean");
                APvarianceLabel.setText("Variance");
                break;
        }
    }
    /**
     * Opens the information window for the supermarket queue simulation.
     */
    @FXML
    private void openInfo() {
        try {
            Stage infoStage = new Stage();
            infoStage.setTitle("Supermarket Simulation Info");

            SupermarketQueueSimulationInfo infoView = new SupermarketQueueSimulationInfo();
            Scene scene = infoView.createInfoScene();

            infoStage.setScene(scene);
            infoStage.show();
        } catch (Exception e) {
            e.printStackTrace(); //
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
            Simulation.Distributions distribution, double meanSP, double varianceSP, double meanAP, double varianceAP) {
        if (servicePointsCount < 1) {
            log("Number of service points should be positive", RED);
            return false;
        }

        if (simulationTime < 1) {
            log("Simulation time should be positive", RED);
            return false;
        }

        if (distribution.equals(Simulation.Distributions.Exponential) && meanSP <= 0.0 ||
                distribution.equals(Simulation.Distributions.Exponential) && meanAP <= 0.0) {
            log("In the Exponential distribution both Mean values should be greater than 0", RED);
            return false;
        }

        if (distribution.equals(Simulation.Distributions.Uniform) && meanSP > varianceSP ||
                distribution.equals(Simulation.Distributions.Uniform) && meanAP > varianceAP) {
            log("In the Uniform distribution both Max values should be greater than Min", RED);
            return false;
        }

        try {
            sim = new Simulation(this, servicePointsCount, customersCount, distribution, meanSP, varianceSP, meanAP, varianceAP);
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
        double meanSP, varianceSP, meanAP, varianceAP;
        try {
            time = Integer.parseInt(simulationTimeComboBox.getValue());
            meanSP = Double.parseDouble(meanSPDistValueTextField.getText());
            varianceSP = Double.parseDouble(varianceSPDistValueTextField.getText());
            meanAP = Double.parseDouble(meanAPDistValueTextField.getText());
            varianceAP = Double.parseDouble(varianceAPDistValueTextField.getText());
        } catch (Exception e) {
            log(SPmeanLabel.getText() + " and " + SPvarianceLabel.getText() + " values should be integers or double values");
            System.out.println(e.getMessage());
            return;
        }

        int customersCount = (int) Math.floor(numOfCustomersSlider.getValue());

        startSimulation(cashiersCount, customersCount, time, distribution, meanSP, varianceSP, meanAP, varianceAP);
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
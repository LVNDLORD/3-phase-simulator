package controller;

import java.io.InputStream;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import model.QueueUpdateListener;
import model.Clock;
import model.ServicePoint;
import model.Simulation;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import java.util.List;

import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import view.*;

/**
 * Controller for the Supermarket Queue Simulator application.
 * This class manages the user interface interactions, updating the simulation based on user inputs,
 * and reflecting the simulation's state on the UI.
 */
public class Controller implements QueueUpdateListener {

    // FXML annotated fields for JavaFX components
    @FXML
    private Slider cashierSlider; // Slider to adjust the number of cashiers.
    // Reference to the log TextArea in the "Logs" section
    @FXML
    private TextArea logTextArea; // TextArea for logging simulation events and messages.
    @FXML
    private ComboBox<String> distributionComboBox; // ComboBox to select the probability distribution type.
    @FXML
    private ComboBox<String> simulationTimeComboBox; // ComboBox to choose the duration of the simulation.
    @FXML
    private TextField meanSPDistValueTextField; // TextField for inputting the mean of the service point distribution.
    @FXML
    private TextField varianceSPDistValueTextField; // TextField for inputting the variance of the service point distribution.
    @FXML
    private TextField meanAPDistValueTextField; // TextField for inputting the mean of the arrival process distribution.
    @FXML
    private TextField varianceAPDistValueTextField; // TextField for inputting the variance of the arrival process distribution.
    @FXML
    private Label SPvarianceLabel; // Label for displaying service point variance.
    @FXML
    private Label SPmeanLabel; // Label for displaying service point mean.
    @FXML
    private Label APvarianceLabel; // Label for displaying arrival process variance.
    @FXML
    private Label APmeanLabel; // Label for displaying arrival process mean.
    @FXML
    private Button helpButton; // Button to display help and information about the simulation.
    @FXML
    private GridPane cashierGrid; // GridPane for arranging cashier elements in the UI.
    @FXML
    private PieChart pieChartResult;

    private String selectedDistribution = "Normal"; // Default distribution type

    @FXML
    private TextFlow logTextFlow;
    @FXML
    private ScrollPane logScrollPane;
    @FXML
    private Accordion accordion;
    public static final String RED = "\033[0;31m";
    private Map<Integer, Label> cashierLabels = new HashMap<>(); // Map to link cashier number with their respective UI label.
    private Map<Integer, Integer> servedCustomersMap = new HashMap<>();
    private Simulation sim; // Instance of the simulation model.

    public Controller() {
        log("Controller initialized");
    }

    /**
     * Initializes the controller. This method is automatically called after the FXML fields are injected.
     */
    public void initialize() {
        pieChartResult.setData(FXCollections.observableArrayList(
                new PieChart.Data("No Data", 0)
        ));
        initializeCashierGrid();
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

    /**
     * Updates the labels for distribution parameters based on the selected distribution type.
     * This method changes the text of labels to reflect the correct parameter names for the chosen distribution.
     *
     * @param selectedDistribution The type of distribution selected by the user.
     *                             Expected values are "Uniform", "Exponential", and "Normal".
     */
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

    /**
     * Initializes the cashier grid layout for the simulation.
     * This method sets up the grid layout with a specific number of rows and columns
     * and adds a cashier box to each cell of the grid.
     */
    private void initializeCashierGrid() {
        int rows = 3; // Assuming 3 rows
        int cols = 3; // Assuming 3 columns

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                int cashierNumber = row * cols + col;
                VBox cashierBox = createCashierBox(cashierNumber);
                cashierGrid.add(cashierBox, col, row);
            }
        }
    }


    /**
     * Updates the visual status of a cashier desk in the simulation grid.
     * This method changes the border color of the cashier box to indicate whether
     * it is active or not.
     *
     * @param row       The row of the cashier desk in the grid.
     * @param col       The column of the cashier desk in the grid.
     * @param isActive  Indicates whether the cashier desk is active.
     */
    private void updateCashierStatus(int row, int col, boolean isActive) {
        VBox cashierBox = (VBox) getNodeFromGridPane(cashierGrid, col, row);
        if (cashierBox != null) {
            String borderColor = isActive ? "green" : "red";
            cashierBox.setStyle("-fx-border-color: " + borderColor +
                    "; -fx-border-width: 3" +
                    "; -fx-padding: 5" +
                    "; -fx-border-radius: 10" + // Adjust the radius value as needed
                    "; -fx-background-radius: 10"); // Same radius for background to match

        }
    }

    /**
     * Creates and returns a VBox representing a cashier desk.
     * This method constructs the visual representation of a cashier desk, including
     * an image and a label for queue information.
     *
     * @param cashierNumber The identifier for the cashier desk.
     * @return VBox representing a cashier desk.
     */
    private VBox createCashierBox(int cashierNumber) {
        ImageView cashierView = new ImageView(new Image("/cashier.png"));
        cashierView.setFitHeight(50); // Adjust size as needed
        cashierView.setFitWidth(50);

        // Create a label for queue info
        Label queueInfo = new Label("Queue: 0 | Served: 0");
        queueInfo.setAlignment(Pos.CENTER);

        // Store the label in the map
        cashierLabels.put(cashierNumber, queueInfo);

        VBox cashierBox = new VBox(10, cashierView, queueInfo); // 10 is the spacing between cashier image and queue info
        cashierBox.setAlignment(Pos.CENTER);
        cashierBox.setStyle("-fx-border-color: red; -fx-border-width: 3; -fx-padding: 5; -fx-border-radius: 10; -fx-background-radius: 10;");

        return cashierBox;
    }

    /**
     * Updates the information displayed on a cashier's label in the UI.
     * This method is called whenever there is a change in the queue size or the number of served customers
     * at a specific cashier desk. It ensures that the update is performed on the JavaFX Application Thread.
     *
     * @param cashierNumber    The identifier for the cashier desk being updated.
     * @param queueLength      The current number of customers in the queue at the cashier desk.
     * @param servedCustomers  The total number of customers served by the cashier desk so far.
     */
    @Override
    public void updateQueueInfo(int cashierNumber, int queueLength, int servedCustomers) {
        Platform.runLater(() -> {
            servedCustomersMap.put(cashierNumber, servedCustomers);
            updatePieChart(servedCustomersMap);
            Label label = cashierLabels.get(cashierNumber);
            if (label != null) {
                String text = "Queue: " + queueLength + " | Served: " + servedCustomers;
                label.setText(text);
            }
        });
    }
    /**
     * Updates the display of cashier desks based on the number of active cashiers.
     *
     * @param activeCount The number of active cashier desks to display.
     */
    private void updateCashierDesks(int activeCount) {
        int rows = 3; // Assuming 3 rows
        int cols = 3; // Assuming 3 columns

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                boolean isActive = (row * cols + col) < activeCount;
                updateCashierStatus(row, col, isActive);
            }
        }
    }

    /**
     * Retrieves a specific node from a GridPane based on its column and row indices.
     * This method iterates through all children of the GridPane and returns the node
     * that matches the specified column and row. If no matching node is found, it returns null.
     *
     * @param gridPane The GridPane from which the node is to be retrieved.
     * @param col      The column index of the node to be retrieved.
     * @param row      The row index of the node to be retrieved.
     * @return The node at the specified column and row indices, or null if no such node exists.
     */
    private Node getNodeFromGridPane(GridPane gridPane, int col, int row) {
        for (Node node : gridPane.getChildren()) {
            if (GridPane.getColumnIndex(node) != null && GridPane.getColumnIndex(node) == col &&
                    GridPane.getRowIndex(node) != null && GridPane.getRowIndex(node) == row) {
                return node;
            }
        }
        return null;
    }
    private void updatePieChart(Map<Integer, Integer> servedCustomersMap) {
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();

        servedCustomersMap.forEach((cashierId, servedCustomers) -> {
            pieChartData.add(new PieChart.Data("Cashier " + (cashierId +1), servedCustomers));
        });

        pieChartResult.setData(pieChartData);
    }



    /**
     * Attempts to launch simulation (model) with given parameters.
     *
     * @param servicePointsCount Number of initial service points
     *
     * @param simulationTime     For how long simulation should run (in simulation
     *                           time, not real time)
     * @param distribution       Distribution type. Possible values are "Normal",
     *                           "Binomial", "Exponential" and "Poisson"
     * @return Boolean value, indicating whether simulation launch succeeded or
     *         failed
     */
    public boolean startSimulation(int servicePointsCount, int simulationTime,
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
            sim = new Simulation(this, servicePointsCount, distribution, meanSP, varianceSP, meanAP, varianceAP);
            sim.setSimulationTime(simulationTime*60);

            // Start simulation in new thread
            Thread thread = new Thread(sim);
            thread.start();
            thread.join();
            int customersServed = sim.getCustomersServed();
            outputResults(customersServed);
            return true;
        } catch (Exception e) {
            log("Failed to launch simulation. Error: " + e.getMessage(), RED);
            return false;
        }
    }

    /**
     * Starts the simulation based on the current settings and user inputs.
     *
     * @param mouseEvent The mouse event triggering the start of the simulation.
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



        startSimulation(cashiersCount, time, distribution, meanSP, varianceSP, meanAP, varianceAP);
        updateCashierDesks(0);
    }

    /**
     * Outputs results of simulation run to the interface
     * @param customersServed Total number of customers served during simulation run
     */
    public void outputResults(int customersServed) {
        log("Simulation ended at " + (int)Clock.getInstance().getClock() + " mins");
        log ("Total customers served: " + customersServed);
    }

    /**
     * Logs a message to the console and the application's log text flow.
     * The message is prefixed with the current system time in HH:mm:ss format.
     * If the log text flow is available, the message is also added to it.
     *
     * @param s The message to be logged. It can be of any object type and will be converted to a string.
     */
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

    /**
     * Logs a message to the console and the application's log text flow with color.
     * The message is prefixed with the current system time in HH:mm:ss format.
     * If the log text flow is available, the message is also added to it with the specified color.
     *
     * @param s     The message to be logged. It can be of any object type and will be converted to a string.
     * @param color The color in which the message should be logged. Should match the color constants defined.
     */
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
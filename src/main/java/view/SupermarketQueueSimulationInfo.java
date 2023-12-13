package view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import java.net.URL;

public class SupermarketQueueSimulationInfo {

        private final int WRAPPING_WIDTH = 900;

        public SupermarketQueueSimulationInfo() {}

        public Scene createInfoScene() {
                GridPane grid = new GridPane();
                grid.setHgap(20);
                grid.setVgap(10);
                grid.setPadding(new Insets(10, 10, 10, 10));
                grid.setAlignment(Pos.TOP_CENTER);

                Text title = new Text("Java Queue Simulator User Guide");
                title.setId("title");
                title.setFill(Color.DARKBLUE);
                grid.add(title, 0, 0);



                // Main description
                Text mainDescription = new Text(
                        "Welcome to the Java Queue Simulator! This tool simulates a queue system with customizable parameters. " +
                                "Ideal for students and professionals alike, it provides insights into how different settings impact queue dynamics.\n\n"
                );
                mainDescription.setId("mainDescription");

// Subtitle: Getting Started
                Text gettingStartedTitle = new Text("Getting Started\n");
                gettingStartedTitle.setId("subtitle");

// Getting Started Description
                Text gettingStartedDescription = new Text(
                        "Choose Your Input Parameters:\n" +
                                "Location: Input settings are located on the left side of the interface.\n" +
                                "Options:\n" +
                                "- Number of Cashiers: Select between 1 to 9 cashiers.\n" +
                                "- Probability Distributions: Choose from Normal, Uniform, or Exponential distributions.\n" +
                                "- Simulation Time: Set the duration from 1 to 12 hours.\n" +
                                "- Service Point and Arrival Process: Input values for mean and variance.\n\n"
                );
                gettingStartedDescription.setId("description");

// Subtitle: Understanding the Simulation Area
                Text simulationAreaTitle = new Text("Understanding the Simulation Area:\n");
                simulationAreaTitle.setId("subtitle");

// Simulation Area Description
                Text simulationAreaDescription = new Text(
                        "Located in the center, this area visualizes the simulation.\n" +
                                "Once cashiers are set, their border color changes to green.\n" +
                                "Below this area is a log that displays console messages during the simulation.\n\n"
                );
                simulationAreaDescription.setId("description");

// Subtitle: Reviewing the Output
                Text outputReviewTitle = new Text("Reviewing the Output:\n");
                outputReviewTitle.setId("subtitle");

// Output Review Description
                Text outputReviewDescription = new Text(
                        "On the right side, you'll find the output section.\n" +
                                "It displays simulation results, including a pie chart.\n" +
                                "The chart shows the total number of customers served and the percentage handled by each cashier.\n\n"
                );
                outputReviewDescription.setId("description");

// Subtitle: Starting the Simulation
                Text startSimulationTitle = new Text("Starting the Simulation:\n");
                startSimulationTitle.setId("subtitle");

// Starting Simulation Description
                Text startSimulationDescription = new Text(
                        "Press the 'Start' button located in the right bottom corner to begin the simulation.\n\n"
                );
                startSimulationDescription.setId("description");

// Subtitle: Conclusion
                Text conclusionTitle = new Text("Conclusion\n");
                conclusionTitle.setId("subtitle");

// Conclusion Description
                Text conclusionDescription = new Text(
                        "With these simple steps, you're ready to explore various queue scenarios. " +
                                "Adjust the settings to see how different configurations affect customer flow and cashier efficiency. Happy simulating!\n"
                );
                conclusionDescription.setId("description");

                Text authorsDescription = new Text("Made by students of Metropolia UAS: Andrii Deshko, Sergio Soares, Anna Linden, Arman Yerkeshev and Kiana Aghajani");

                // For subtitles
                gettingStartedTitle.getStyleClass().add("subtitle");
                simulationAreaTitle.getStyleClass().add("subtitle");
                outputReviewTitle.getStyleClass().add("subtitle");
                startSimulationTitle.getStyleClass().add("subtitle");
                conclusionTitle.getStyleClass().add("subtitle");
                authorsDescription.getStyleClass().add("subtitle");

                // For descriptions
                gettingStartedDescription.getStyleClass().add("description");
                simulationAreaDescription.getStyleClass().add("description");
                outputReviewDescription.getStyleClass().add("description");
                startSimulationDescription.getStyleClass().add("description");
                conclusionDescription.getStyleClass().add("description");
                authorsDescription.getStyleClass().add("description");


                // Adding text elements to the GridPane
                grid.add(mainDescription, 0, 1);
                grid.add(gettingStartedTitle, 0, 2);
                grid.add(gettingStartedDescription, 0, 3);
                grid.add(simulationAreaTitle, 0, 4);
                grid.add(simulationAreaDescription, 0, 5);
                grid.add(outputReviewTitle, 0, 6);
                grid.add(outputReviewDescription, 0, 7);
                grid.add(startSimulationTitle, 0, 8);
                grid.add(startSimulationDescription, 0, 9);
                grid.add(conclusionTitle, 0, 10);
                grid.add(conclusionDescription, 0, 11);
                grid.add(authorsDescription, 0, 12);


                grid.setStyle("-fx-background-color: #f0f0f0;");

                Scene scene = new Scene(grid);
                URL stylesheetURL = getClass().getResource("/css/Style.css");
                if (stylesheetURL != null) {
                        scene.getStylesheets().add(stylesheetURL.toExternalForm());
                } else {
                        System.out.println("Stylesheet not found");
                }
                return scene;
        }
}

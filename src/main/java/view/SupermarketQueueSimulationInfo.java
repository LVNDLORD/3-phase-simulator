package view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

/**
 * This class is responsible for creating and displaying the information view
 * for the supermarket queue simulation. It includes details about the simulation
 * and credits to the creators of the project. This view is an integral part of
 * the user interface of the simulation application.
 *
 * The class utilizes JavaFX components to create a visually appealing information
 * scene which can be displayed within the application's window.
 *
 * @author Arman Yerkeshev, Andrii Deshko, Dias Soares Sérgio, Anna Lindén, Aghajani Kiana
 * @version 1.0
 */
public class SupermarketQueueSimulationInfo {

    /**
     * The width used for wrapping text elements in the information scene.
     * This constant ensures that the text content is neatly formatted within
     * the bounds of the application window.
     */
    private final int WRAPPING_WIDTH = 900;
/**
 * Constructs a new instance of SupermarketQueueSimulationInfo.
 * This constructor initializes the information view with default settings.
 */
public SupermarketQueueSimulationInfo() {}

/**
 * Creates and returns a Scene containing the information view for the
 * supermarket queue simulation. This method sets up the layout and adds
 * text elements containing details about the simulation and its creators.
 *
 * The method arranges the text elements on a GridPane and applies styling
 * to ensure a clean and organized presentation.
 *
 * @return The Scene object representing the information view of the simulation.
 */
public Scene createInfoScene() {
        GridPane grid = new GridPane();
        grid.setHgap(20);
        grid.setVgap(10);
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setAlignment(Pos.TOP_CENTER);

        Text title = new Text("Supermarket Queue Simulation");
        title.setId("title");
        title.setFill(Color.DARKBLUE);

        Text description = new Text(
        "This simulation aims to analyze and optimize supermarket queue management. " +
        "Various factors like queue length, service time, and customer satisfaction can be studied. " +
        "Further details about the simulation will be added as the project progresses."
        );
        description.setWrappingWidth(WRAPPING_WIDTH);
        description.setId("infoText");

        Text authors = new Text(
        "Project Team: Arman Yerkeshev, Andrii Deshko, Dias Soares Sérgio, Anna Lindén, Aghajani Kiana"
        );
        authors.setWrappingWidth(WRAPPING_WIDTH);
        authors.setId("infoText");

        grid.add(title, 0, 0);
        grid.add(description, 0, 1);
        grid.add(authors, 0, 2);

        grid.setStyle("-fx-background-color: #f0f0f0;");

        Scene scene = new Scene(grid);
        scene.getStylesheets().add(getClass().getResource("/css/Style.css").toExternalForm());
        return scene;
        }
        }


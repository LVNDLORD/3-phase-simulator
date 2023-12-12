package view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.image.Image;

/**
 * The View class extends the Application class from JavaFX.
 * This class is responsible for starting the JavaFX application and
 * setting up the initial stage (window) with the necessary configurations.
 */
public class View extends Application {
    /**
     * Starts the JavaFX application by setting up the primary stage.
     * This method loads the FXML layout, sets the application icon, and displays the stage.
     *
     * @param stage The primary stage for this application, onto which
     *              the application scene can be set. Stages are top-level containers.
     * @throws Exception if there is any issue in loading the FXML file or setting the scene.
     */
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/interface.fxml"));
        Parent root = fxmlLoader.load();



                // Set the icon
        Image applicationIcon = new Image(getClass().getResourceAsStream("/icon.png"));
        stage.getIcons().add(applicationIcon);

        stage.setScene(new Scene(root));
        stage.show();
    }
}


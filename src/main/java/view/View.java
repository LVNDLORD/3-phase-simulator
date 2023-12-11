package view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.image.Image;

public class View extends Application {
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


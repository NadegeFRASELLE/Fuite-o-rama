package fr.ul.miage.nf.baignoire;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.logging.Logger;

public class App extends Application {
    private static final Logger LOG = Logger.getLogger(App.class.getName());
    private static Scene scene;

    /**
     * Chargement de la ressource
     */
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("/fuite-o-rama.fxml"));
        scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
        stage.setTitle("Fuite-O-Rama");
        stage.setResizable(false);
        stage.show();
    }
    // Lancer l'application|
    public static void main(String[] args) {
        launch();
    }
}

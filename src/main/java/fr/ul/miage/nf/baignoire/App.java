package fr.ul.miage.nf.baignoire;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Classe qui permet de démarrer la scène principale et de démarrer l'interface graphique JavaFX de l'application..
 */
public class App extends Application {

    /**
     * La scène principale de Fuite-o-Rama.
     */
    private static Scene scene;

    /**
     * Méthode principale qui lance l'application en utilisant la méthode launch() de la classe JavaFX.
     *
     * @param args les arguments passés à la méthode via une ligne de commande.
     */
    public static void main(String[] args) {
        launch();
    }

    /**
     * Méthode qui permet de démarrer la scène à partir d'un fichier FXML.
     * Héritée d'Application et redéfinie.
     *
     * @param stage La scène principale de l'application.
     * @throws IOException Si une erreur se produit lors du chargement du fichier FXML.
     */
    @Override
    public void start(Stage stage) throws IOException {
        // Récupération du fichier FMXL
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("/fuite-o-rama.fxml"));
        // Mise en place de la scène principale.
        scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
        stage.setTitle("Fuite-O-Rama");
        //Verrouillage de la taille de la fenêtre pour une meilleure expérience avec l'interface.
        stage.setResizable(false);
        stage.show();
    }
}

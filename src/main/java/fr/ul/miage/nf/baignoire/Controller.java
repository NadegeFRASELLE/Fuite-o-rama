package fr.ul.miage.nf.baignoire;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;

import java.util.logging.Logger;

public class Controller {
    private static final Logger LOG = Logger.getLogger(Controller.class.getName());
    private static final int MAXIMUM_FUITES = 5;
    private static final int MAXIMUM_ROBINETS = 5;

    @FXML
    Button createSimulationBtn;

    @FXML
    TextField robinetsTF;

    @FXML
    TextField contenanceBTF;

    @FXML
    TextField fuitesTF;

    @FXML
    TabPane optTP;

    @FXML
    public void initialize() {
        //Bouton de création de simulation bloqué tant que le nombre de fuites et le nombre de robinets n'est pas renseigné.
        createSimulationBtn.setDisable(true);

        fuitesTF.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                fuitesTF.setText(newValue.replaceAll("\\D", ""));
            }
            actualiseCreationSimulationBtnState();
        });
        robinetsTF.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                robinetsTF.setText(newValue.replaceAll("\\D", ""));
            }
            actualiseCreationSimulationBtnState();
        });
        contenanceBTF.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("^\\d*\\.?\\d*$")) {
                contenanceBTF.setText(newValue.replaceAll("[^\\d.]", ""));
            }
            actualiseCreationSimulationBtnState();
        });

    }

    @FXML
    Simulation createSimulationBtnAction() {
        Simulation simulation = contenanceBTF.getText().isBlank() ?
                new Simulation(Integer.parseInt(robinetsTF.getText()),
                        Integer.parseInt(fuitesTF.getText())) :
                new Simulation(Integer.parseInt(robinetsTF.getText()),
                        Integer.parseInt(fuitesTF.getText()),
                        Double.parseDouble(contenanceBTF.getText()));
        LOG.info("Simulation créée avec succès avec : " + simulation.robinets.size() + " robinets, "
                + simulation.fuites.size() + " fuites et une baignoire d'une capacité de "
                + simulation.baignoire.getCapacite() + " L.");
        goToNextTab();
        return simulation;
    }

    private boolean isCreationSimulationDisabled() {
        return fuitesTF.getText().isBlank() || robinetsTF.getText().isBlank();
    }

    private void actualiseCreationSimulationBtnState() {
        createSimulationBtn.setDisable(isCreationSimulationDisabled());
    }

    private void goToNextTab() {
        optTP.getSelectionModel().selectNext();
    }

}

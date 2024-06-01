package fr.ul.miage.nf.baignoire;

import javafx.application.Platform;
import javafx.concurrent.ScheduledService;
import javafx.concurrent.WorkerStateEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.shape.Rectangle;
import javafx.stage.Window;
import javafx.util.Duration;

import java.time.Instant;
import java.util.Arrays;
import java.util.Map;
import java.util.logging.Logger;

public class Controller {
    private static final Logger LOG = Logger.getLogger(Controller.class.getName());

    private final int MAXIMUM_FUITES = 5;
    private final int MAXIMUM_ROBINETS = 5;

    private Simulation simulation = null;

    Fuite currentlySelectedFuite = null;

    @FXML
    Button createSimulationBtn;

    @FXML
    Button boutonDemarrer;

    @FXML
    Button btnReparation;

    @FXML
    Rectangle baignoireImage;

    @FXML
    TextField robinetsTF;

    @FXML
    TextField contenanceBTF;

    @FXML
    TextField fuitesTF;

    @FXML
    TabPane optTP;

    @FXML
    ListView<Robinet> listeRobinets;

    @FXML
    ListView<Fuite> listeFuites;

    @FXML
    TextArea infosTA;

    @FXML
    NumberAxis xAxis = new NumberAxis();

    @FXML
    NumberAxis yAxis = new NumberAxis();

    @FXML
    LineChart<Number, Number> graphReport = new LineChart<>(xAxis, yAxis);

    @FXML
    Button btnCSV;

    @FXML
    Button clearGraphBtn;

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

        listeRobinets.setOnMouseClicked(event -> {
            if (event.getClickCount() == 1) {
                Robinet r = listeRobinets.getSelectionModel().getSelectedItem();
                showDialogRobinet(r, listeRobinets.getScene().getWindow());
            }
        });

        listeFuites.setOnMouseClicked(event -> {
            if (event.getClickCount() == 1 && !simulation.isSimulationRunning) {
                Fuite f = listeFuites.getSelectionModel().getSelectedItem();
                showDialogFuite(f, listeFuites.getScene().getWindow());
            }
            if (event.getClickCount() == 1 && simulation.isSimulationRunning) {
                currentlySelectedFuite = listeFuites.getSelectionModel().getSelectedItem();
            }
        });
        infosTA.setWrapText(true);

    }
    @FXML
    public void action_demarrer() {
        if(this.simulation == null) {
            return;
        }
        Instant top = Instant.now(); //top chronomètre
        boutonDemarrer.setDisable(true); //on désactive le bouton pendant le processus
        runningSimulation(top);
    }

    public void runningSimulation(Instant debutSimulation) {
        simulation.isSimulationRunning = true;
        simulation.baignoire.setVolume(0);
        simulation.simulationReport = new SimulationReport(simulation.robinets, simulation.fuites, debutSimulation);
        simulation.robinets.forEach(robinet -> {
            robinet.setOnSucceeded((WorkerStateEvent e) -> {
                simulation.writeInSimulationReport();
                fillOrEmptyBaignoire();
                baignoirePleineProcedure(robinet);
            });
            robinet.setPeriod(Duration.seconds(1));
            robinet.reset();
            robinet.start();
        });
        simulation.fuites.forEach(fuite -> {
            fuite.setOnSucceeded((WorkerStateEvent e) -> {
                simulation.writeInSimulationReport();
                fillOrEmptyBaignoire();
                baignoirePleineProcedure(fuite);
            });
            fuite.setPeriod(Duration.seconds(1));
            fuite.reset();
            fuite.start();
        });

    }

    private void fillOrEmptyBaignoire() {
        baignoireImage.setHeight(simulation.baignoire.getVolume());
    }

    private void baignoirePleineProcedure(ScheduledService<Baignoire> service) {
        if(simulation.baignoire.estPleine()) {
            service.cancel();
            if(simulation.isSimulationRunning) { //si la simulation est encore en cours, on l'arrete (mais une seule fois)
                simulation.updateFuitesFinSimulation(listeFuites.getItems());
                simulation.simulationReport.finSimulation = Instant.now();
                buildLineChart();
                infosTA.setText(simulation.simulationReport.afficherInfosSimulation());
                simulation.isSimulationRunning = false;
                boutonDemarrer.setDisable(false);
            }
        }
    }



    private void showDialogRobinet(Robinet robinet, Window owner) {
        String titrePopUpRobinet = "Modifications";
        String headerTextRobinet = "Modifications du robinet n°" + robinet.getIdRobinet() + ": " ;
        String contentTextRobinet = "Nouveau débit désiré : ";
        String placeHolderRobinet = String.valueOf(robinet.getDebitRobinet());

        TextInputDialog dialog = createTextInputDialog(
                robinet.getIdRobinet(),
                titrePopUpRobinet,
                headerTextRobinet,
                contentTextRobinet,
                placeHolderRobinet,
                owner);

        dialog.showAndWait().ifPresent(newValue -> {
            if (newValue.matches("^\\d*\\.?\\d*$")) {
                robinet.setDebitRobinet(Double.parseDouble(newValue));
                LOG.info("Nouveau débit du robinet n° " + robinet.getIdRobinet() + ": " +  robinet.getDebitRobinet());
                //la liste est mise à jour après modification du débit.
                listeRobinets.refresh();
            }
                });
    }

    private void showDialogFuite(Fuite fuite, Window owner) {
        String titrePopUpRobinet = "Modifications";
        String headerTextRobinet = "Modifications de la fuite n°" + fuite.getIdFuite() + ": " ;
        String contentTextRobinet = "Nouveau débit désiré : ";
        String placeHolderRobinet = String.valueOf(fuite.getDebitFuite());

        TextInputDialog dialog = createTextInputDialog(
                fuite.getIdFuite(),
                titrePopUpRobinet,
                headerTextRobinet,
                contentTextRobinet,
                placeHolderRobinet,
                owner);

        dialog.showAndWait().ifPresent(newValue -> {
            if (newValue.matches("^\\d*\\.?\\d*$")) {
                fuite.setDebitFuite(Double.parseDouble(newValue));
                LOG.info("Nouveau débit de la fuite n° " + fuite.getIdFuite() + ": " +  fuite.getDebitFuite());
                //la liste est mise à jour après modification du débit.
                listeFuites.refresh();
            }
        });
    }



    private static <T> TextInputDialog createTextInputDialog(T value, String title, String header, String content, String placeholder, Window owner) {
        TextInputDialog dialog = new TextInputDialog(String.valueOf(value));

        dialog.setTitle(title);
        dialog.setHeaderText(header);
        dialog.setContentText(content);
        dialog.getEditor().setText(placeholder);
        dialog.initOwner(owner);

        return dialog;
    }

    @FXML
    void createSimulationBtnAction() {
        // hum, a modifier
        if (createSimulationBtn.isDisabled()) {
            return;
        }
        this.simulation = null;
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
        initialiserListeRobinets(simulation);
        initialiserListeFuites(simulation);
        this.simulation = simulation;
    }

    private void initialiserListeRobinets(Simulation simulation) {
        listeRobinets.getItems().clear();
        listeRobinets.getItems().setAll(simulation.robinets);
    }

    private void initialiserListeFuites(Simulation simulation) {
        listeFuites.getItems().clear();
        listeFuites.getItems().setAll(simulation.fuites);
    }

    private boolean isCreationSimulationDisabled() {
        return fuitesTF.getText().isBlank() || robinetsTF.getText().isBlank()
                || Integer.parseInt(fuitesTF.getText()) > MAXIMUM_FUITES ||
                Integer.parseInt(robinetsTF.getText()) > MAXIMUM_ROBINETS;
    }

    private void actualiseCreationSimulationBtnState() {
        createSimulationBtn.setDisable(isCreationSimulationDisabled());
    }

    private void goToNextTab() {
        optTP.getSelectionModel().selectNext();
    }

    @FXML
    private void patchFuite(){
        this.simulation.reparerFuite(currentlySelectedFuite);
        listeFuites.getItems().remove(currentlySelectedFuite);
        currentlySelectedFuite = null;
        listeFuites.refresh();
    }

    @FXML
    private void clearGraph() {
        graphReport.getData().clear();
    }

    private void buildLineChart() {
        graphReport.setTitle("Évolution du volume de la baignoire");
        xAxis.setLabel("Moment (en millisecondes");
        yAxis.setLabel("Volume de la baignoire");
        XYChart.Series series = new XYChart.Series();
        series.setName("Simulation du " + Instant.now());
        simulation.simulationReport.reportingData.forEach((key, value) -> series.getData().add(new XYChart.Data<>(key, value)));
        graphReport.getData().add(series);
    }

    @FXML
    private void btnCSVAction() {
        simulation.simulationReport.generateCSVFile();
    }

}

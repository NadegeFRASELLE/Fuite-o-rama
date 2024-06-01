package fr.ul.miage.nf.baignoire;

import javafx.concurrent.ScheduledService;
import javafx.concurrent.WorkerStateEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Rectangle;
import javafx.stage.Window;
import javafx.util.Duration;

import java.time.Instant;
import java.util.List;
import java.util.logging.Logger;

/**
 * Classe Controller, qui permet de piloter l'interface graphique.
 */
public class Controller {

    /**
     * Logger utilisé pour surveiller le bon déroulement de l'exécution.
     */
    private static final Logger LOG = Logger.getLogger(Controller.class.getName());

    /**
     * Le nombre maximum de fuites autorisées dans la simulation.
     */
    private final int MAXIMUM_FUITES = 10;

    /**
     * Le nombre maximum de robinets autorisées dans la simulation.
     */
    private final int MAXIMUM_ROBINETS = 10;
    /**
     * Bouton qui permet de créer une simulation.
     */
    @FXML
    Button createSimulationBtn;
    /**
     * Bouton qui permet de démarrer la simulation.
     */
    @FXML
    Button boutonDemarrer;

    // Attributs JavaFX
    /**
     * Bouton qui permet de réparer la fuite sélectionnée dans la liste de fuites.
     * Ne fonctionne que lorsque la simulation est en marche.
     */
    @FXML
    Button btnReparation;
    /**
     * Bouton qui permet de générer et télécharger un fichier CSV qui contient les informations sur la courbe de
     * remplissage de la baignoire lors de la dernière simulation.
     */
    @FXML
    Button btnCSV;
    /**
     * Si on lance plusieurs simulations à la suite, on peut comparer leurs graphes. Ce bouton permet de vider le
     * graphique de ses séries de données.
     */
    @FXML
    Button clearGraphBtn;
    /**
     * Le rectangle qui montre la progression du remplissage de la baignoire au cours de la simulation.
     */
    @FXML
    Rectangle baignoireImage;
    /**
     * Le champ qui permet de renseigner le nombre de robinets qu'on veut avoir dans la simulation.
     */
    @FXML
    TextField robinetsTF;
    /**
     * Le champ qui permet de renseigner la contenance qu'on veut assigner à la baignoire dans notre simulation.
     * Ce champ n'est pas obligatoire.
     */
    @FXML
    TextField contenanceBTF;
    /**
     * Le champ qui permet de renseigner le nombre de fuites qu'on veut avoir dans la simulation.
     */
    @FXML
    TextField fuitesTF;
    /**
     * Le panneau à onglets.
     */
    @FXML
    TabPane optTP;
    /**
     * La liste des robinets de la simulation, visibles sous forme de liste.
     */
    @FXML
    ListView<Robinet> listeRobinets;
    /**
     * La liste des fuites de la simulation, visibles sous forme de liste.
     */
    @FXML
    ListView<Fuite> listeFuites;
    /**
     * La zone de texte qui contient les informations sur la simulation une fois qu'elle est terminée.
     */
    @FXML
    TextArea infosTA;
    /**
     * L'axe X du graphique généré après la simulation. Indique le moment en millisecondes.
     */
    @FXML
    NumberAxis xAxis = new NumberAxis();
    /**
     * L'axe Y du graphique généré après la simulation. Indique le volume de la baignoire en litres.
     */
    @FXML
    NumberAxis yAxis = new NumberAxis();
    /**
     * Le graphique généré après la simulation.
     */
    @FXML
    LineChart<Number, Number> graphReport = new LineChart<>(xAxis, yAxis);
    /**
     * La photo de la baignoire qui est visible à la place du rectangle lorsque la simulation est éteinte.
     */
    @FXML
    ImageView photoBaignoire;
    /**
     * Instance de la simulation utilisée dans le Controller.
     */
    private Simulation simulation = null;
    /**
     * La fuite qui est actuellement sélectionnée dans la liste lorsque la simulation est en marche.
     */
    private Fuite currentlySelectedFuite = null;

    /**
     * Méthode d'initialisation de la scène.
     * Elle définit le comportement initial de certains éléments de l'interface graphique.
     * Elle ajoute également des écouteurs d'événements sur les listes de robinets et de fuites,
     * ainsi que des comportements quand on clique sur les listes.
     */
    @FXML
    void initialize() {
        //Bouton de création de simulation bloqué tant que le nombre de fuites et le nombre de robinets n'est pas renseigné.
        createSimulationBtn.setDisable(true);
        baignoireImage.setVisible(false);
        photoBaignoire.setVisible(true);
        infosTA.setWrapText(true);

        setListener(fuitesTF, "\\d*", "\\D");
        setListener(robinetsTF, "\\d*", "\\D");
        setListener(contenanceBTF, "^\\d*\\.?\\d*$", "[^\\d.]");

        listeRobinets.setOnMouseClicked(event -> {
            if (event.getClickCount() == 1) {
                Robinet r = listeRobinets.getSelectionModel().getSelectedItem();
                showDialogRobinet(r, listeRobinets.getScene().getWindow());
            }
        });

        listeFuites.setOnMouseClicked(event -> {
            // si la simulation n'a pas commencé, on peut changer le débit de la fuite.
            if (event.getClickCount() == 1 && !simulation.isSimulationRunning) {
                Fuite f = listeFuites.getSelectionModel().getSelectedItem();
                showDialogFuite(f, listeFuites.getScene().getWindow());
            }
            //une fois que la simulation a commencé, on ne peut plus le modifier.
            if (event.getClickCount() == 1 && simulation.isSimulationRunning) {
                currentlySelectedFuite = listeFuites.getSelectionModel().getSelectedItem();
            }
        });

    }

    /**
     * Méthode utilisée pour démarrer la simulation, si elle a été paramétrée auparavant.
     */
    @FXML
    void action_demarrer() {
        if (this.simulation == null) {
            return;
        }
        Instant top = Instant.now(); //top chronomètre
        boutonDemarrer.setDisable(true); //on désactive le bouton pendant le processus
        runningSimulation(top);
    }

    /**
     * Méthode utilisée pour créer une simulation, et initialiser les listes graphiques de robinets et fuites.
     */
    @FXML
    void createSimulationBtnAction() {
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

    /**
     * Méthode utilisée pour réparer une fuite. Quand elle est réparée, elle est retirée de la liste.
     */
    @FXML
    void patchFuite() {
        this.simulation.reparerFuite(currentlySelectedFuite);
        listeFuites.getItems().remove(currentlySelectedFuite);
        currentlySelectedFuite = null;
        listeFuites.refresh();
    }

    /**
     * Méthode qui permet de vider le graphique des anciennes simulations de la session.
     */
    @FXML
    void clearGraph() {
        graphReport.getData().clear();
    }

    /**
     * Méthode utilisée pour générer et télécharger un fichier CSV à partir du rapport de la simulation.
     */
    @FXML
    void btnCSVAction() {
        if (simulation == null) {
            return;
        }
        simulation.simulationReport.generateCSVFile();
    }

    /**
     * Méthode qui crée une boîte de dialogue qui prend les inputs utilisateurs avec les paramètres spécifiés.
     *
     * @param <T> le type de la valeur initiale de la boîte de dialogue
     * @param value la valeur initiale de la boîte de dialogue
     * @param title le titre de la boîte de dialogue
     * @param header le texte d'en-tête de la boîte de dialogue
     * @param content le texte de contenu de la boîte de dialogue
     * @param placeholder le texte d'espace réservé affiché dans la zone de texte
     * @param owner la fenêtre propriétaire de la boîte de dialogue
     * @return la boîte de dialogue créée
     */
    private <T> TextInputDialog createTextInputDialog(T value, String title, String header, String content, String placeholder, Window owner) {
        TextInputDialog dialog = new TextInputDialog(String.valueOf(value));
        dialog.setTitle(title);
        dialog.setHeaderText(header);
        dialog.setContentText(content);
        dialog.getEditor().setText(placeholder);
        dialog.initOwner(owner);
        return dialog;
    }

    /**
     * Méthode utilisée pour mettre en place un écouteur d'événements sur un TextField, afin de filtrer le type
     * d'entrées qu'il accepte.
     * @param tf le champ de texte sur lequel on veut ajouter un écouteur.
     * @param regex le schéma que l'input doit respecter.
     * @param remplacement le schéma des données qu'il faut remplacer.
     */
    private void setListener(TextField tf, String regex, String remplacement) {
        tf.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches(regex)) {
                tf.setText(newValue.replaceAll(remplacement, ""));
            }
            actualiseCreationSimulationBtnState();
        });
    }

    /**
     * Démarre la simulation en utilisant les paramètres spécifiés.
     *
     * @param debutSimulation l'instant de début de la simulation
     */
    private void runningSimulation(Instant debutSimulation) {
        simulation.isSimulationRunning = true;
        // on change l'image visible, et la baignoire laisse place au rectangle.
        baignoireImage.setVisible(true);
        photoBaignoire.setVisible(false);
        //le volume est initialisé à 0.
        simulation.baignoire.setVolume(0);
        // on crée un rapport de simulation, afin que les tasks puissent écrire dedans.
        simulation.simulationReport = new SimulationReport(simulation.robinets, simulation.fuites, debutSimulation);
        setOnSucceededScheduledServices(simulation.robinets);
        setOnSucceededScheduledServices(simulation.fuites);
    }

    /**
     * On définit le comportement attendu d'un service préparé lorsqu'il réussit sa tache préparée.
     * @param serviceList une liste d'objets qui héritent de ScheduledService<Baignoire> (Robinet ou Fuite)
     */
    private void setOnSucceededScheduledServices(List<? extends ScheduledService<Baignoire>> serviceList) {
        serviceList.forEach(service -> {
            service.setOnSucceeded((WorkerStateEvent e) -> {
                simulation.writeInSimulationReport();
                fillOrEmptyBaignoire();
                baignoirePleineProcedure(service);
            });
            // on devra attendre une seconde entre chaque tache.
            service.setPeriod(Duration.seconds(1));
            service.reset();
            // on démarre le service
            service.start();
        });
    }

    /**
     * Méthode utilisée pour mettre à jour la hauteur d'eau sur le rectangle après que le service ait effectué sa tâche.
     */
    private void fillOrEmptyBaignoire() {
        baignoireImage.setHeight(simulation.baignoire.getVolume());
    }

    /**
     * Procédure à appliquer quand la baignoire est remplie afin d'arrêter la simulation.
     * @param service le service préparé pour Baignoire.
     */
    private void baignoirePleineProcedure(ScheduledService<Baignoire> service) {
        if (simulation.baignoire.estPleine()) {
            service.cancel();
            procedureEteindreSimulation();
        }
    }

    /**
     * La procédure à appliquer une fois que la baignoire est pleine. Elle ne sera appliquée qu'une seule fois.
     */
    private void procedureEteindreSimulation() {
        if (simulation.isSimulationRunning) { //si la simulation est encore en cours, on l'arrete (mais une seule fois)
            simulation.eteindreSimulation(listeFuites.getItems());
            buildLineChart();
            infosTA.setText(simulation.simulationReport.afficherInfosSimulation());
            baignoireImage.setVisible(false);
            photoBaignoire.setVisible(true);
            boutonDemarrer.setDisable(false);
            goToNextTab();
        }
    }

    /**
     * Méthode utilisée pour afficher une boîte de dialogue pour modifier le débit d'un robinet.
     *
     * @param robinet le robinet dont on veut modifier le débit.
     * @param owner la fenêtre propriétaire de la boîte de dialogue.
     */
    private void showDialogRobinet(Robinet robinet, Window owner) {
        String titrePopUpRobinet = "Modifications";
        String headerTextRobinet = "Modifications du robinet n°" + robinet.getIdRobinet() + ": ";
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
                LOG.info("Nouveau débit du robinet n° " + robinet.getIdRobinet() + ": " + robinet.getDebitRobinet());
                //la liste est mise à jour après modification du débit.
                listeRobinets.refresh();
            }
        });
    }

    /**
     * Méthode utilisée pour afficher une boîte de dialogue pour modifier le débit d'une fuite.
     *
     * @param fuite la fuite dont on veut modifier le débit.
     * @param owner la fenêtre propriétaire de la boîte de dialogue.
     */
    private void showDialogFuite(Fuite fuite, Window owner) {
        String titrePopUpRobinet = "Modifications";
        String headerTextRobinet = "Modifications de la fuite n°" + fuite.getIdFuite() + ": ";
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
                LOG.info("Nouveau débit de la fuite n° " + fuite.getIdFuite() + ": " + fuite.getDebitFuite());
                //la liste est mise à jour après modification du débit.
                listeFuites.refresh();
            }
        });
    }

    /**
     * Méthode utilisée pour initialiser la liste des robinets sur l'interface graphique.
     * @param simulation la simulation actuelle.
     */
    private void initialiserListeRobinets(Simulation simulation) {
        listeRobinets.getItems().clear();
        listeRobinets.getItems().setAll(simulation.robinets);
    }

    /**
     * Méthode utilisée pour initialiser la liste des fuites sur l'interface graphique.
     * @param simulation la simulation actuelle.
     */
    private void initialiserListeFuites(Simulation simulation) {
        listeFuites.getItems().clear();
        listeFuites.getItems().setAll(simulation.fuites);
    }

    /**
     * Méthode utilisée pour décider si le bouton de création de simulation doit être désactivé ou non.
     */
    private boolean isCreationSimulationDisabled() {
        return fuitesTF.getText().isBlank() || robinetsTF.getText().isBlank()
                || Integer.parseInt(fuitesTF.getText()) > MAXIMUM_FUITES ||
                Integer.parseInt(robinetsTF.getText()) > MAXIMUM_ROBINETS;
    }

    /**
     * Méthode utilisée pour mettre à jour le statut du bouton de création de simulation (activé ou désactivé).
     */
    private void actualiseCreationSimulationBtnState() {
        createSimulationBtn.setDisable(isCreationSimulationDisabled());
    }

    /**
     * Méthode utilisée pour afficher l'onglet suivant et ainsi diriger l'utilisateur sur l'application.
     */
    private void goToNextTab() {
        optTP.getSelectionModel().selectNext();
    }

    /**
     * Méthode utilisée pour construire le graphique à partir du rapport de simulation une fois qu'elle est terminée.
     */
    private void buildLineChart() {
        graphReport.setTitle("Évolution du volume de la baignoire");
        xAxis.setLabel("Moment (en millisecondes");
        yAxis.setLabel("Volume de la baignoire");
        //création d'une série de données.
        XYChart.Series series = new XYChart.Series();
        series.setName("Simulation du " + Instant.now());
        //ajout des données au graphique
        simulation.simulationReport.reportingData.forEach((key, value) -> series.getData().add(new XYChart.Data<>(key, value)));
        graphReport.getData().add(series);
    }

}

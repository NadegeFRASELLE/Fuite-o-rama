package fr.ul.miage.nf.baignoire;


import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.FileWriter;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Logger;

public class SimulationReport {
    /**
     * Logger utilisé pour surveiller le bon déroulement de l'exécution.
     */
    private static final Logger LOG = Logger.getLogger(SimulationReport.class.getName());
    /**
     * Injection de l'instance de la classe Baignoire.
     */
    Baignoire baignoire = Baignoire.getInstance();
    /**
     * Liste qui contient tous les robinets de la simulation.
     */
    List<Robinet> robinets;
    /**
     * Liste qui contient toutes les fuites au début de la simulation.
     */
    List<Fuite> fuitesStart;
    /**
     * Liste qui contient toutes les fuites à la fin de la simulation.
     */
    List<Fuite> fuitesEnd = new ArrayList<>();
    /**
     * Le timestamp du moment où la simulation démarre.
     */
    Instant debutSimulation;
    /**
     * Le timestamp du moment où la simulation termine.
     */
    Instant finSimulation = null;
    /**
     * Les données du rapport, qui contient la différence avec le timestamp du début de la simulation en clef et le
     * volume de la baignoire à ce moment.
     */
    Map<Long, Double> reportingData = new TreeMap<>();

    /**
     * Constructeur du SimulationReport.
     *
     * @param robinets        la liste des robinets de la simulation.
     * @param fuitesStart     la liste des fuites au début de la simulation.
     * @param debutSimulation l'instant de début de la simulation.
     */
    public SimulationReport(List<Robinet> robinets, List<Fuite> fuitesStart, Instant debutSimulation) {
        this.robinets = robinets;
        this.fuitesStart = fuitesStart;
        this.debutSimulation = debutSimulation;
    }

    /**
     * Méthode utilisée pour calculer la différence entre le début de la simulation et maintenant pour les clefs
     * des valeurs de reportingData.
     *
     * @return {Long} la différence en millisecondes.
     */
    public Long calculReportTime() {
        return Duration.between(debutSimulation, Instant.now()).toMillis();
    }

    /**
     * Méthode utilisée pour afficher le message d'information à la fin de la simulation. Il contient un résumé de ce
     * qui est arrivé lors de la simulation.
     *
     * @return {String} le message composé.
     */
    public String afficherInfosSimulation() {
        return "Le remplissage a pris " + calculerDureeSimulation() + " millisecondes, soit " + convertMsToMinutes() +
                "\n" +
                robinets.size() + " robinets ont aidé à remplir cette baignoire de " + baignoire.getCapacite() + " litres.\n" +
                "Il y avait " + fuitesStart.size() + " fuites au début, et " + fuitesEnd.size() + " fuites à la fin. \n" +
                (fuitesStart.size() - fuitesEnd.size()) + " fuite(s) ont été réparée(s).";
    }

    /**
     * Génère un fichier CSV contenant les données de rapport de la simulation.
     * Le fichier généré reste actuellement dans le fichier source Fuite-O-Rama.
     *
     * @throws IOException si une erreur d'E/S se produit lors de l'écriture dans le fichier CSV.
     */
    public void generateCSVFile() {

        String csvFilePath = "rapport_simulation_" + Instant.now() + ".csv";

        try (FileWriter fileWriter = new FileWriter(csvFilePath);
             CSVPrinter csvPrinter = new CSVPrinter(fileWriter, CSVFormat.DEFAULT)) {
            csvPrinter.printRecord("Moment(ms)", "Volume Baignoire");
            reportingData.forEach((key, value) -> {
                try {
                    csvPrinter.printRecord(key, value);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
            csvPrinter.flush();
            LOG.info("Le fichier CSV a été créé avec succès à : " + csvFilePath);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Méthode utilisée pour calculer la durée totale de la simulation en millisecondes pour le message d'information.
     *
     * @return {String} le message généré.
     */
    private String calculerDureeSimulation() {
        return String.valueOf(Duration.between(debutSimulation, finSimulation).toMillis());
    }

    /**
     * Méthode utilisée pour convertir la durée de la simulation en minutes, secondes et millisecondes.
     *
     * @return la durée de la simulation en minutes, secondes et millisecondes.
     */
    private String convertMsToMinutes() {
        long millisecondes = Duration.between(debutSimulation, finSimulation).toMillis();
        long minutes = (millisecondes / (1000 * 60)) % 60;
        long seconds = (millisecondes / 1000) % 60;
        long remainingMilliseconds = millisecondes % 1000;
        return minutes + " minutes, " + seconds + " secondes et " + remainingMilliseconds + " millisecondes.";
    }

}

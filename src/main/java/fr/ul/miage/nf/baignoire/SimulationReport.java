package fr.ul.miage.nf.baignoire;


import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.FileWriter;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.logging.Logger;

public class SimulationReport {
    Baignoire baignoire = Baignoire.getInstance();
    List<Robinet> robinets;
    List<Fuite> fuitesStart;
    List<Fuite> fuitesEnd = new ArrayList<>();
    Instant debutSimulation;
    Instant finSimulation = null;
    // TreeMap car c'est rangé par ordre croissant des clefs
    Map<Long, Double> reportingData = new TreeMap<>();
    private static final Logger LOG = Logger.getLogger(SimulationReport.class.getName());

    public SimulationReport(List<Robinet> robinets, List<Fuite> fuitesStart, Instant debutSimulation) {
        this.robinets = robinets;
        this.fuitesStart = fuitesStart;
        this.debutSimulation = debutSimulation;
    }

    public Long calculReportTime() {
        return Duration.between(debutSimulation, Instant.now()).toMillis();
    }

    public String afficherInfosSimulation() {
        return "Le remplissage a pris " + calculerDureeSimulation() + " millisecondes, soit " + convertMsToMinutes() +
                "\n" +
                robinets.size() + " robinets ont aidé à remplir cette baignoire de " + baignoire.getCapacite() + " litres.\n" +
                "Il y avait " + fuitesStart.size() + " fuites au début, et " + fuitesEnd.size() + " fuites à la fin. \n" +
                (fuitesStart.size() - fuitesEnd.size()) + " fuite(s) ont été réparée(s).";
    }

    public void generateCSVFile() {
        // Chemin vers le fichier CSV de sortie
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

    private String calculerDureeSimulation() {
        return String.valueOf(Duration.between(debutSimulation, finSimulation).toMillis());
    }

    private String convertMsToMinutes(){
        long millisecondes = Duration.between(debutSimulation, finSimulation).toMillis();
        long minutes = ( millisecondes/ (1000 * 60)) % 60;
        long seconds = (millisecondes / 1000) % 60;
        long remainingMilliseconds = millisecondes % 1000;
        return minutes + " minutes, " + seconds + " secondes et " + remainingMilliseconds + " millisecondes.";
    }

}

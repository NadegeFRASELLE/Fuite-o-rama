package fr.ul.miage.nf.baignoire;


import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SimulationReport {
    List<Robinet> robinets;
    List<Fuite> fuitesStart;
    List<Fuite> fuitesEnd = new ArrayList<>();
    Instant debutSimulation;
    Instant finSimulation = null;
    Map<String, String> reportingData = new HashMap<>();

    public SimulationReport(List<Robinet> robinets, List<Fuite> fuitesStart, Instant debutSimulation) {
        this.robinets = robinets;
        this.fuitesStart = fuitesStart;
        this.debutSimulation = debutSimulation;
    }

    public String calculReportTime(){
        return String.valueOf(Duration.between(debutSimulation, Instant.now()));
    }





}

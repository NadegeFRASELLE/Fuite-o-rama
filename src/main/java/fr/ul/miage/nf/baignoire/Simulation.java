package fr.ul.miage.nf.baignoire;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Simulation {
    Baignoire baignoire = Baignoire.getInstance();
    List<Robinet> robinets = new ArrayList<>();
    List<Fuite> fuites = new ArrayList<>();
    boolean isSimulationRunning = false;

//  ScheduledExecutorService pool = Executors.newScheduledThreadPool(calculerTailleThreadPool());
    private static final Logger LOG = Logger.getLogger(Simulation.class.getName());

    public Simulation(int numRobinets, int numFuites, double capaciteBaignoire) {
        baignoire.setCapacite(capaciteBaignoire);
        LOG.info("La baignoire a désormais une capacité de " + baignoire.getCapacite());
        robinets = creationRobinets(numRobinets);
        LOG.info(robinets.size() + " robinets ont été créés.");
        fuites = creationFuites(numFuites);
        LOG.info(fuites.size() + " fuites ont été créées.");
    }

    public Simulation(int numRobinets, int numFuites) {
        LOG.info("La baignoire a désormais une capacité de " + baignoire.getCapacite());
        robinets = creationRobinets(numRobinets);
        LOG.info(robinets.size() + " robinets ont été créés.");
        fuites = creationFuites(numFuites);
        LOG.info(fuites.size() + " fuites ont été créées.");
    }

    public void reparerFuite(Fuite fuite) {
        fuite.cancel();
        LOG.info("La fuite n°" + fuite.getIdFuite() + " a été réparée.");
    }

    private ArrayList<Robinet> creationRobinets(int nbRobinets) {
        return IntStream.range(0,nbRobinets)
                .mapToObj(i -> new Robinet(i+1))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    private ArrayList<Fuite> creationFuites(int nbFuites) {
        return IntStream.range(0, nbFuites)
                .mapToObj(i -> new Fuite (i+1))
                .collect(Collectors.toCollection(ArrayList::new));
    }

}

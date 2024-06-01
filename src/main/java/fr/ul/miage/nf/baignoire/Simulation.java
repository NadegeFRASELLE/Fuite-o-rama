package fr.ul.miage.nf.baignoire;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * La classe Simulation, qui regroupe les éléments nécessaires à une simulation.
 */
public class Simulation {
    /**
     * Logger utilisé pour surveiller le bon déroulement de l'exécution.
     */
    private static final Logger LOG = Logger.getLogger(Simulation.class.getName());
    /**
     * Injection de l'instance de la classe Baignoire.
     */
    Baignoire baignoire = Baignoire.getInstance();
    /**
     * Liste qui contient tous les robinets de la simulation.
     */
    List<Robinet> robinets = new ArrayList<>();
    /**
     * Liste qui contient toutes les fuites de la simulation.
     */
    List<Fuite> fuites = new ArrayList<>();
    /**
     * Le rapport de simulation dans lequel les ScheduledService Baignoire écrivent.
     */
    SimulationReport simulationReport;
    /**
     * Booléen qui indique si la simulation est actuellement en cours d'exécution ou non.
     */
    boolean isSimulationRunning = false;

    /**
     * Constructeur de Simulation utilisé si l'utilisateur a renseigné une capacité pour la baignoire.
     *
     * @param numRobinets       le nombre de robinets à créer pour la simulation
     * @param numFuites         le nombre de fuites à créer pour la simulation
     * @param capaciteBaignoire la capacité de la baignoire pour la simulation
     */
    public Simulation(int numRobinets, int numFuites, double capaciteBaignoire) {
        baignoire.setCapacite(capaciteBaignoire);
        LOG.info("La baignoire a désormais une capacité de " + baignoire.getCapacite());
        robinets = creationRobinets(numRobinets);
        LOG.info(robinets.size() + " robinets ont été créés.");
        fuites = creationFuites(numFuites);
        LOG.info(fuites.size() + " fuites ont été créées.");
    }

    /**
     * Constructeur de Simulation utilisé si l'utilisateur n'a pas renseigné une capacité pour la baignoire.
     *
     * @param numRobinets le nombre de robinets à créer pour la simulation
     * @param numFuites   le nombre de fuites à créer pour la simulation
     */
    public Simulation(int numRobinets, int numFuites) {
        LOG.info("La baignoire a désormais une capacité de " + baignoire.getCapacite());
        robinets = creationRobinets(numRobinets);
        LOG.info(robinets.size() + " robinets ont été créés.");
        fuites = creationFuites(numFuites);
        LOG.info(fuites.size() + " fuites ont été créées.");
    }

    /**
     * Méthode utilisée pour réparer une fuite.
     *
     * @param fuite la fuite qu'on va réparer.
     */
    public void reparerFuite(Fuite fuite) {
        fuite.cancel();
        LOG.info("La fuite n°" + fuite.getIdFuite() + " a été réparée.");
    }

    /**
     * Méthode que les ScheduledService Baignoire utilisent pour écrire dans le rapport quand ils ont fini leur tâche préparée.
     */
    public void writeInSimulationReport() {
        simulationReport.reportingData.put(simulationReport.calculReportTime(), baignoire.getVolume());

    }

    /**
     * Méthode qui termine la simulation et termine le rapport de simulation.
     *
     * @param listeFuites la liste des fuites à la fin de la simulation.
     */
    public void eteindreSimulation(List<Fuite> listeFuites) {
        simulationReport.fuitesEnd = listeFuites;
        simulationReport.finSimulation = Instant.now();
        isSimulationRunning = false;
    }

    /**
     * Méthode utilisée pour créer les robinets des constructeurs.
     *
     * @param nbRobinets le nombre de robinets à créer.
     * @return la liste des robinets qu'on vient de créer.
     */
    private List<Robinet> creationRobinets(int nbRobinets) {
        return IntStream.range(0, nbRobinets)
                .mapToObj(i -> new Robinet(i + 1))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * Méthode utilisée pour créer les fuites des constructeurs.
     *
     * @param nbFuites le nombre de robinets à créer.
     * @return la liste des fuites qu'on vient de créer.
     */
    private List<Fuite> creationFuites(int nbFuites) {
        return IntStream.range(0, nbFuites)
                .mapToObj(i -> new Fuite(i + 1))
                .collect(Collectors.toCollection(ArrayList::new));
    }


}

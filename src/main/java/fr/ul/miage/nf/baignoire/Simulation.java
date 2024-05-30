package fr.ul.miage.nf.baignoire;

import java.util.LinkedList;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Simulation {
    Baignoire baignoire = Baignoire.getInstance();
    LinkedList<Robinet> robinets = new LinkedList<>();
    LinkedList<Fuite> fuites = new LinkedList<>();
    private static final Logger LOG = Logger.getLogger(Simulation.class.getName());

    public Simulation(int numRobinets, int numFuites, double capacitéBaignoire) {
        baignoire.setCapacite(capacitéBaignoire);
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

    private void reparerFuite(int idFuite) {
        //réparer fuite ==> débit = 0 ? Fermer fuite ?
    }
    private void changerDebitRobinet(int idRobinet, double debit) {
        Robinet robinet = robinets.get(idRobinet);
        robinet.setDebitRobinet(debit);
        System.out.println("Le débit du robinet " + (idRobinet +1) + " est passé à " + robinet.getDebitRobinet() + "litres par minutes.");
    }

    private LinkedList<Robinet> creationRobinets(int nbRobinets) {
        return Stream.generate(Robinet::new)
                .limit(nbRobinets)
                .collect(Collectors.toCollection(LinkedList::new));
    }

    private LinkedList<Fuite> creationFuites(int nbFuites) {
        return Stream.generate(Fuite::new)
                .limit(nbFuites)
                .collect(Collectors.toCollection(LinkedList::new));
    }



}

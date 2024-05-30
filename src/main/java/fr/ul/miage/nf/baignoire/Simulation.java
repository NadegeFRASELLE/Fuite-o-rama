package fr.ul.miage.nf.baignoire;

import java.util.LinkedList;
import java.util.logging.Logger;

public class Simulation {
    Baignoire baignoire = Baignoire.getInstance();
    LinkedList<Robinet> robinets = new LinkedList<>();
    LinkedList<Fuite> fuites = new LinkedList<>();
    private static final Logger LOG = Logger.getLogger(Simulation.class.getName());


}

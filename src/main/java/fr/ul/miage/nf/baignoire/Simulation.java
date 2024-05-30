package fr.ul.miage.nf.baignoire;

import java.util.LinkedList;
import java.util.logging.Logger;

public class Simulation {
    Baignoire baignoire = Baignoire.getInstance();
    LinkedList<Robinet> robinets = new LinkedList<>();
    LinkedList<Fuite> fuites = new LinkedList<>();
    private static final Logger LOG = Logger.getLogger(Simulation.class.getName());

    private static final int MAXIMUM_FUITES = 5;
    private static final int MAXIMUM_ROBINETS = 5;

    private void reparerFuite(int idFuite) {
        //réparer fuite ==> débit = 0 ? Fermer fuite ?
    }
    private void changerDebitRobinet(int idRobinet, double debit) {
        //get debit robinet ==> nv debit = debit
    }
}

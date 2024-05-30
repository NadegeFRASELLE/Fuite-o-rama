package fr.ul.miage.nf.baignoire;

import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;

import java.util.logging.Logger;

public class Fuite extends ScheduledService<Baignoire> {

    private double debitFuite;

    private final Baignoire baignoire = Baignoire.getInstance();

    private final double DEBIT_MAX_FUITE = 50;

    private final double DEBIT_DEFAUT_FUITE = 3;

    private static final Logger LOG = Logger.getLogger(Fuite.class.getName());

    public Fuite() {
        this.debitFuite = DEBIT_DEFAUT_FUITE;
    }

    @Override
    protected Task<Baignoire> createTask() {
        return null;
    }

    public double getDebitFuite() {
        return debitFuite;
    }

    public void setDebitFuite(double debitFuite) {
        this.debitFuite = Math.max(1, Math.min(debitFuite, this.DEBIT_MAX_FUITE));
    }
}

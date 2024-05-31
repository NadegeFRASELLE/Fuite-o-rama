package fr.ul.miage.nf.baignoire;
import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;

import java.util.logging.Logger;

public class Robinet extends ScheduledService<Baignoire> {
    private int idRobinet;

    private double debitRobinet;

    private final Baignoire baignoire = Baignoire.getInstance();

    private final double DEBIT_MAX_ROBINET = 100;

    private final double DEBIT_DEFAUT_ROBINET = 6;

    private static final Logger LOG = Logger.getLogger(Robinet.class.getName());

    public Robinet(int idRobinet) {
        this.idRobinet = idRobinet;
        this.debitRobinet = DEBIT_DEFAUT_ROBINET;
    }

    @Override
    protected Task<Baignoire> createTask() {
        return new Task<Baignoire>() {
            @Override
            protected Baignoire call() {
                synchronized (baignoire) {
                    baignoire.remplir(debitRobinet);
                }
                return baignoire;
            }
        };
    }

    public int getIdRobinet() {
        return idRobinet;
    }

    public double getDebitRobinet() {
        return debitRobinet;
    }

    public void setDebitRobinet(double debitRobinet) {
        this.debitRobinet = Math.max(1, Math.min(debitRobinet, DEBIT_MAX_ROBINET));
    }

    @Override
    public String toString() {
        return "Robinet n°" + idRobinet + " : " + debitRobinet + " L / minute";
    }
}

package fr.ul.miage.nf.baignoire;

import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;

/**
 * La classe Robinet.
 */
public class Robinet extends ScheduledService<Baignoire> {

    /**
     * Injection de l'instance de la classe Baignoire.
     */
    private final Baignoire baignoire = Baignoire.getInstance();

    /**
     * Le débit maximum que peut avoir un robinet.
     */
    private final double DEBIT_MAX_ROBINET = 100;

    /**
     * Le débit par défaut d'un robinet à sa création.
     */
    private final double DEBIT_DEFAUT_ROBINET = 6;

    /**
     * L'identifiant du robinet, pour le retrouver dans la liste des robinets.
     */
    private int idRobinet;

    /**
     * Le débit du robinet.
     */
    private double debitRobinet;

    /**
     * Constructeur de Robinet.
     *
     * @param idRobinet l'identifiant qu'on veut donner au robinet.
     */
    public Robinet(int idRobinet) {
        this.idRobinet = idRobinet;
        this.debitRobinet = DEBIT_DEFAUT_ROBINET;
    }

    /**
     * La tâche préparée que le robinet va effectuer quand on lui donne le top départ.
     *
     * @return {Task Baignoire} La tâche préparée pour la classe Baignoire.
     */
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

    // Getters et Setters

    /**
     * Getter de l'identifiant du robinet.
     *
     * @return {int} l'identifiant du robinet.
     */
    public int getIdRobinet() {
        return idRobinet;
    }

    /**
     * Getter du débit du robinet.
     *
     * @return {double} le débit du robinet.
     */
    public double getDebitRobinet() {
        return debitRobinet;
    }

    /**
     * Setter qui définit le débit de la fuite.
     *
     * @param debitRobinet le nouveau débit du robinet.
     */
    public void setDebitRobinet(double debitRobinet) {
        this.debitRobinet = Math.max(1, Math.min(debitRobinet, DEBIT_MAX_ROBINET));
    }

    // Fin des Getters et Setters

    /**
     * Redéfinition de la méthode toString du robinet.
     *
     * @return une description textuelle du robinet.
     */
    @Override
    public String toString() {
        return "Robinet n°" + idRobinet + " : " + debitRobinet + " L / minute";
    }
}

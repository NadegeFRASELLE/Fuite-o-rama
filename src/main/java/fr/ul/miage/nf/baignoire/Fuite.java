package fr.ul.miage.nf.baignoire;

import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;

/**
 * La classe Fuite
 */
public class Fuite extends ScheduledService<Baignoire> {

    /**
     * Injection de l'instance de la classe Baignoire.
     */
    private final Baignoire baignoire = Baignoire.getInstance();

    /**
     * Le débit maximum que peut avoir une fuite.
     */
    private final double DEBIT_MAX_FUITE = 50;

    /**
     * Le débit par défaut d'une fuite à sa création.
     */
    private final double DEBIT_DEFAUT_FUITE = 3;

    /**
     * L'identifiant de la fuite, pour le retrouver dans la liste des fuites.
     */
    private int idFuite;

    /**
     * Le débit de la fuite.
     */
    private double debitFuite;

    /**
     * Constructeur de Fuite.
     *
     * @param idFuite l'identifiant qu'on veut donner à la fuite.
     */
    public Fuite(int idFuite) {
        this.idFuite = idFuite;
        this.debitFuite = DEBIT_DEFAUT_FUITE;
    }

    /**
     * La tâche préparée que la fuite va effectuer quand on lui donne le top départ.
     *
     * @return {Task<Baignoire>} La tâche préparée pour la classe Baignoire.
     */
    @Override
    protected Task<Baignoire> createTask() {
        return new Task<Baignoire>() {
            @Override
            protected Baignoire call() {
                synchronized (baignoire) {
                    baignoire.fuiter(debitFuite);
                }
                return baignoire;
            }
        };
    }

    // Getters et Setters

    /**
     * Getter de l'identifiant de la fuite.
     *
     * @return {int} l'identifiant de la fuite.
     */
    public int getIdFuite() {
        return idFuite;
    }

    /**
     * Getter du débit de la fuite.
     *
     * @return {double} le débit de la fuite.
     */
    public double getDebitFuite() {
        return debitFuite;
    }

    /**
     * Setter qui définit le débit de la fuite.
     *
     * @param debitFuite le nouveau débit de la fuite
     */
    public void setDebitFuite(double debitFuite) {
        this.debitFuite = Math.max(1, Math.min(debitFuite, this.DEBIT_MAX_FUITE));
    }

    // Fin des Getters et Setters

    /**
     * Redéfinition de la méthode toString de la fuite.
     *
     * @return une description textuelle de la fuite.
     */
    @Override
    public String toString() {
        return "Fuite n°" + idFuite + ": " + debitFuite + " L / minute";
    }
}

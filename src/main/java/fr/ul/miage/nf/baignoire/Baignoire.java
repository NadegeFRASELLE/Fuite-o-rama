package fr.ul.miage.nf.baignoire;

import java.util.logging.Logger;

public class Baignoire {

    private double capacite = CAPACITE_DEFAUT;

    private double volume;

    /**
     * L'unique instance autorisée de la classe baignoire.
     */
    private static Baignoire INSTANCE;

    public static final double CAPACITE_DEFAUT = 200;

    public static final double CAPACITE_MAX_BAIGNOIRE = 500;

    private static final Logger LOG = Logger.getLogger(Baignoire.class.getName());

    private Baignoire() {
    }

    public static Baignoire getInstance() {
        if(INSTANCE == null) {
            INSTANCE = new Baignoire();
        }
        return INSTANCE;
    }

    public boolean estPleine() {
        return capacite <= volume;
    }

    public void remplir(double debitRobinet) {
        volume = Math.min(volume + debitRobinet, capacite);
    }

    public void fuiter(double debitFuite) {
        volume = Math.max(volume - debitFuite, 0);
    }

    public double getCapacite() {
        return capacite;
    }

    public double getVolume() {
        return volume;
    }

    public void setCapacite(double capacite) {
        this.capacite = Math.max(0, Math.min(capacite, CAPACITE_MAX_BAIGNOIRE));
    }

    public void setVolume(double volume) {
        this.volume = volume;
    }
}

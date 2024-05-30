package fr.ul.miage.nf.baignoire;

import java.util.logging.Logger;

public class Baignoire {

    private double capacite = CAPACITE_DEFAUT;

    private double volume = 0.0;

    /**
     * L'unique instance autorisée de la classe baignoire.
     */
    private static Baignoire INSTANCE;

    public static final double CAPACITE_DEFAUT = 200;

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



}

package fr.ul.miage.nf.baignoire;

/**
 * La classe Baignoire.
 */
public class Baignoire {

    /**
     * La capacité par défaut de la baignoire, si elle n'est pas spécifiée par l'utilisateur.
     */
    public static final double CAPACITE_DEFAUT = 200;
    /**
     * La capacité maximum qu'on peut donner à la baignoire.
     * Ce nombre a été choisi pour que le remplissage de la baignoire ne dépasse pas sur d'autres éléments.
     */
    public static final double CAPACITE_MAX_BAIGNOIRE = 500;
    /**
     * L'unique instance autorisée de la classe baignoire.
     */
    private static Baignoire INSTANCE;
    /**
     * La capacité de la baignoire.
     */
    private double capacite = CAPACITE_DEFAUT;
    /**
     * La quantité d'eau actuellement contenue dans la baignoire. Débute à 0.
     */
    private double volume;

    /**
     * Le constructeur de la classe Baignoire. Il n'est pas accessible en dehors de la classe, pour qu'il ne puisse pas
     * y avoir plusieurs instances de Baignoire.
     */
    private Baignoire() {
    }

    /**
     * Méthode utilisée pour passer l'instance de Baignoire aux autres classes.
     *
     * @return {Baignoire} L'unique instance de Baignoire.
     */
    public static Baignoire getInstance() {
        // Si aucune instance de Baignoire n'existe, on en créé une et on lo stocke dans INSTANCE
        if (INSTANCE == null) {
            INSTANCE = new Baignoire();
        }
        return INSTANCE;
    }

    /**
     * Méthode utilisée pour contrôler si la baignoire est pleine ou non, afin qu'elle ne puisse pas déborder.
     *
     * @return {boolean} true si la baignoire est pleine, false sinon.
     */
    public boolean estPleine() {
        return capacite <= volume;
    }

    /**
     * Méthode utilisée pour remplir la baignoire.
     *
     * @param debitRobinet la quantité d'eau que le robinet veut ajouter à la baignoire.
     */
    public void remplir(double debitRobinet) {
        // Pour ne pas que la baignoire ne déborde, vérifie qu'ajouter toute l'eau ne fera pas déborder la baignoire.
        volume = Math.min(volume + debitRobinet, capacite);
    }

    /**
     * Méthode utilisée pour retirer de l'eau de la baignoire.
     *
     * @param debitFuite la quantité d'eau que la fuite veut retirer à la baignoire.
     */
    public void fuiter(double debitFuite) {
        // On vérifie que retirer l'intégralité de l'eau ne fera pas passer le volume en dessous de 0.
        volume = Math.max(volume - debitFuite, 0);
    }

    // Getters et setters

    /**
     * Getter de l'attribut capacité.
     *
     * @return la capacité actuelle de la baignoire.
     */
    public double getCapacite() {
        return capacite;
    }

    /**
     * Setter de l'attribut capacité.
     * <p>
     * La capacité doit être comprise entre 0 et la capacité maximale de la baignoire.
     * Si la capacité fournie est inférieure à 0, la capacité sera mise à 0.
     * Si la capacité fournie est supérieure à la capacité maximale de la baignoire,
     * la capacité sera mise à la capacité maximale de la baignoire.
     *
     * @param capacite La nouvelle capacité de la baignoire.
     */
    public void setCapacite(double capacite) {
        this.capacite = Math.max(0, Math.min(capacite, CAPACITE_MAX_BAIGNOIRE));
    }

    /**
     * Getter de l'attribut volume.
     *
     * @return le volume d'eau actuellement dans la baignoire.
     */
    public double getVolume() {
        return volume;
    }

    /**
     * Le setter de la capacité volume.
     *
     * @param volume le nouveau volume de la baignoire.
     */
    public void setVolume(double volume) {
        this.volume = volume;
    }
}

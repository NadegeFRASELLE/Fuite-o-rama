package fr.ul.miage.nf.baignoire;

import java.util.logging.Logger;

/**
 * Classe qui permet de lancer l'application Fuite-O-Rama.
 */
public class Launcher {

    /**
     * Logger utilisé pour surveiller le bon déroulement de l'exécution.
     */
    private static final Logger LOG = Logger.getLogger(Launcher.class.getName());

    /**
     * Méthode utilisée pour appeler la class App. Nécessaire pour pouvoir démarrer l'application.
     * @param args les arguments passés à la méthode via une ligne de commande.
     */
    public static void main(String[] args) {
        LOG.info(demarrage());
        App.main(args);
    }

    /**
     * Méthode appelée au démarrage de l'application.
     * @return le message de bienvenue préparé.
     */
    private static String demarrage() {
        return """
                 \s
                 ,---..-. .-.,-. _______ ,---.          .---.           ,---.    .--.            .--.  \s
                 | .-'| | | ||(||__   __|| .-'         / .-. )          | .-.\\  / /\\ \\ |\\    /| / /\\ \\ \s
                 | `-.| | | |(_)  )| |   | `-.____.___ | | |(_)____.___ | `-'/ / /__\\ \\|(\\  / |/ /__\\ \\\s
                 | .-'| | | || | (_) |   | .-'`----==='| | | | `----==='|   (  |  __  |(_)\\/  ||  __  |\s
                 | |  | `-')|| |   | |   |  `--.       \\ `-' /          | |\\ \\ | |  |)|| \\  / || |  |)|\s
                 )\\|  `---(_)`-'   `-'   /( __.'        )---'           |_| \\)\\|_|  (_)| |\\/| ||_|  (_)\s
                (__)                    (__)           (_)                  (__)       '-'  '-'        \s
                
                Version 1.0 - Juin 2024
                """;
    }

}

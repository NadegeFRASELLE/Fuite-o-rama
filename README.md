# Fuite-O-Rama 💧🛁

Fuite-O-Rama est une simulation de remplissage de baignoire trouée qui permet d'obtenir une courbe de remplissage et un CSV à la fin de la simulation.

# Lancer le projet
## Prérequis
Avant toute chose, il vous faudra [Java Development Kit (JDK) 17](https://www.oracle.com/fr/java/technologies/downloads/#java17) sur votre machine.

## Distribution Binaire
Ouvrez un terminal et naviguez vers le répertoire du projet :

   ```
cd chemin/vers/Fuite-O-Rama
   ```
Ensuite, rendez-vous dans le dossier bin du dossier "bindist", afin de trouver la distribution binaire du projet.

```
cd bindist/bin
```
Enfin, lancez le projet : 

```
./baignoire
```
# Recompiler le projet
## Prérequis

Si vous rencontrez un soucis et souhaitez recompiler le projet, vous aurez besoin de [Apache Maven](https://maven.apache.org/download.cgi).

## Procédure

Exécutez la commande suivante pour reconstruire le projet avec Maven :

   ```
   mvn clean install
   ```

Cette commande téléchargera les dépendances nécessaires, compilera le projet et empaquetera l'application.

## Informations supplémentaires
- Le projet utilise la bibliothèque [JavaFX](https://openjfx.io/) pour créer une interface graphique élégante et moderne.
- La bibliothèque [Apache Commons CSV](https://commons.apache.org/proper/commons-csv/) est utilisée pour générer un fichier CSV avec le rapport de fin de simulation.
- Les images utilisées dans ce projet sont toutes libres de droit et ont été trouvées sur le site internet [Freepik](https://www.freepik.com/)


_Dernière mise à jour : [02/06/2024]_
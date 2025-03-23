package com.labyrinthe;

import java.util.List;

/**
 * Point d'entrée de l'application en mode console pour tester la génération et la résolution de labyrinthes.
 */
public class Main {
    /**
     * Méthode principale pour exécuter un test de génération et de résolution de labyrinthe.
     *
     * @param args les arguments de la ligne de commande (non utilisés ici)
     */
    public static void main(String[] args) {
        int largeur = 19; // Largeur fixe du labyrinthe
        int hauteur = 10; // Hauteur fixe du labyrinthe

        // Affichage d'un en-tête pour le test
        System.out.println("\n=== Test avec un labyrinthe " + largeur + "x" + hauteur + " ===");
        Labyrinthe labyrinthe = new Labyrinthe(hauteur, largeur); // Création d'un labyrinthe aléatoire
        System.out.println("Labyrinthe initial :");
        labyrinthe.afficherLabyrinthe(); // Affichage du labyrinthe initial

        Solver solver = new Solver(labyrinthe); // Initialisation du solveur pour ce labyrinthe

        // Résolution avec BFS
        long startTimeBFS = System.currentTimeMillis(); // Mesure du temps de début
        List<Integer[]> cheminBFS = solver.resoudreBFS(labyrinthe); // Résolution avec l'algorithme BFS
        long durationBFS = System.currentTimeMillis() - startTimeBFS; // Calcul de la durée d'exécution

        // Affichage du chemin BFS si trouvé
        if (!cheminBFS.isEmpty()) {
            labyrinthe.marquerChemin(cheminBFS); // Marquer le chemin sur la grille
            System.out.println("\nLabyrinthe avec chemin BFS (+ pour le chemin) :");
            labyrinthe.afficherLabyrinthe(); // Afficher le labyrinthe avec le chemin
            labyrinthe.reinitialiserMarques(); // Réinitialiser les marques pour la prochaine résolution
        }

        // Résolution avec DFS
        long startTimeDFS = System.currentTimeMillis(); // Mesure du temps de début
        List<Integer[]> cheminDFS = solver.resoudreDFS(labyrinthe); // Résolution avec l'algorithme DFS
        long durationDFS = System.currentTimeMillis() - startTimeDFS; // Calcul de la durée d'exécution

        // Affichage du chemin DFS si trouvé
        if (!cheminDFS.isEmpty()) {
            labyrinthe.marquerChemin(cheminDFS); // Marquer le chemin sur la grille
            System.out.println("\nLabyrinthe avec chemin DFS (+ pour le chemin) :");
            labyrinthe.afficherLabyrinthe(); // Afficher le labyrinthe avec le chemin
        }

        // Affichage des résultats et comparaison
        afficherResultats(solver, cheminBFS, durationBFS, cheminDFS, durationDFS);
    }

    /**
     * Affiche les résultats des algorithmes BFS et DFS, y compris les performances et les statistiques.
     *
     * @param solver      l'instance du solveur utilisée
     * @param cheminBFS   le chemin trouvé par BFS (peut être vide)
     * @param durationBFS la durée d'exécution de BFS en millisecondes
     * @param cheminDFS   le chemin trouvé par DFS (peut être vide)
     * @param durationDFS la durée d'exécution de DFS en millisecondes
     */
    private static void afficherResultats(Solver solver, List<Integer[]> cheminBFS, long durationBFS,
                                          List<Integer[]> cheminDFS, long durationDFS) {
        // Résultats pour BFS
        if (!cheminBFS.isEmpty()) {
            System.out.println("\nBFS a pris " + durationBFS + " ms"); // Temps d'exécution
            System.out.println("Chemin BFS : " + cheminBFS.size() + " étapes"); // Longueur du chemin
            System.out.println("Cellules explorées par BFS : " + solver.getCellulesExploreesBFS()); // Nombre de cellules visitées
        } else {
            System.out.println("\nAucun chemin trouvé avec BFS !"); // Message en cas d'échec
        }

        // Résultats pour DFS
        if (!cheminDFS.isEmpty()) {
            System.out.println("DFS a pris " + durationDFS + " ms"); // Temps d'exécution
            System.out.println("Chemin DFS : " + cheminDFS.size() + " étapes"); // Longueur du chemin
            System.out.println("Cellules explorées par DFS : " + solver.getCellulesExploreesDFS()); // Nombre de cellules visitées
        } else {
            System.out.println("Aucun chemin trouvé avec DFS !"); // Message en cas d'échec
        }

        // Comparaison des performances entre BFS et DFS
        System.out.println("\nComparaison des performances :");
        if (durationBFS < durationDFS) {
            System.out.println("BFS est plus rapide que DFS.");
        } else if (durationBFS > durationDFS) {
            System.out.println("DFS est plus rapide que BFS.");
        } else {
            System.out.println("BFS et DFS ont la même performance.");
        }
    }
}

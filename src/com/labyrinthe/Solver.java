package com.labyrinthe;

import java.util.*;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
import javafx.scene.control.Label;
import javafx.scene.media.MediaPlayer;

/**
 * Gère la résolution du labyrinthe avec les algorithmes BFS et DFS, avec ou sans animation.
 */
public class Solver {
    private int cellulesExploreesBFS = 0; // Nombre de cellules explorées par BFS
    private int cellulesExploreesDFS = 0; // Nombre de cellules explorées par DFS
    private final Labyrinthe labyrinthe; // Labyrinthe à résoudre
    private final LabyrintheRenderer renderer; // Renderer pour l'animation (peut être null)
    private final MediaPlayer mediaPlayer; // Lecteur audio pour le son de succès (peut être null)

    private long tempsExecutionBFS; // Temps d'exécution de BFS en millisecondes
    private int nombreEtapesBFS; // Nombre d'étapes effectuées par BFS
    private long tempsExecutionDFS; // Temps d'exécution de DFS en millisecondes
    private int nombreEtapesDFS; // Nombre d'étapes effectuées par DFS

    /**
     * Construit un solveur avec support pour l'animation et le son.
     *
     * @param labyrinthe  le labyrinthe à résoudre
     * @param renderer    le renderer pour afficher l'animation
     * @param mediaPlayer le lecteur audio pour le son de succès
     */
    public Solver(Labyrinthe labyrinthe, LabyrintheRenderer renderer, MediaPlayer mediaPlayer) {
        this.labyrinthe = labyrinthe;
        this.renderer = renderer;
        this.mediaPlayer = mediaPlayer;
    }

    /**
     * Construit un solveur sans animation ni son, pour une résolution en mode console.
     *
     * @param labyrinthe le labyrinthe à résoudre
     */
    public Solver(Labyrinthe labyrinthe) {
        this.labyrinthe = labyrinthe;
        this.renderer = null;
        this.mediaPlayer = null;
    }

    /**
     * Anime la résolution du labyrinthe avec l'algorithme BFS.
     *
     * @param delai           le délai entre chaque étape en millisecondes
     * @param tempsLabel      le label pour afficher le temps d'exécution
     * @param etapesLabel     le label pour afficher le nombre d'étapes
     * @param cellulesLabel   le label pour afficher le nombre de cellules explorées
     * @param comparaisonLabel le label pour afficher la comparaison BFS/DFS
     */
    public void animerBFS(int delai, Label tempsLabel, Label etapesLabel, Label cellulesLabel, Label comparaisonLabel) {
        renderer.reinitialiserAffichage(); // Réinitialiser l'affichage graphique

        char[][] grid = labyrinthe.getGrid();
        int startX = labyrinthe.getStartX();
        int startY = labyrinthe.getStartY();
        int endX = labyrinthe.getEndX();
        int endY = labyrinthe.getEndY();

        Queue<Integer[]> queue = new LinkedList<>(); // File pour BFS
        Map<String, Integer[]> predecesseurs = new HashMap<>(); // Suivi des prédécesseurs pour reconstruire le chemin
        boolean[][] visite = new boolean[grid.length][grid[0].length]; // Tableau des cellules visitées

        queue.add(new Integer[]{startX, startY});
        visite[startX][startY] = true;

        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}}; // Directions : haut, bas, gauche, droite

        Timeline timeline = new Timeline(); // Animation avec JavaFX
        timeline.setCycleCount(Timeline.INDEFINITE); // Boucle infinie jusqu'à arrêt explicite

        long startTime = System.currentTimeMillis(); // Temps de début
        nombreEtapesBFS = 0;
        cellulesExploreesBFS = 1; // Compter la cellule de départ

        timeline.getKeyFrames().add(new KeyFrame(Duration.millis(delai), event -> {
            if (!queue.isEmpty()) {
                Integer[] current = queue.poll(); // Récupérer la prochaine cellule
                int x = current[0];
                int y = current[1];

                renderer.mettreAJourCase(x, y, "explore"); // Marquer comme explorée
                nombreEtapesBFS++;

                if (x == endX && y == endY) { // Si la sortie est atteinte
                    timeline.stop();
                    List<Integer[]> chemin = reconstruireChemin(predecesseurs, endX, endY);
                    afficherChemin(chemin); // Afficher le chemin final
                    jouerSon(); // Jouer le son de succès

                    tempsExecutionBFS = System.currentTimeMillis() - startTime;
                    mettreAJourLabels(tempsLabel, etapesLabel, cellulesLabel, "BFS"); // Mettre à jour les stats
                    updateComparaison(comparaisonLabel); // Mettre à jour la comparaison
                    return;
                }

                explorerVoisins(queue, visite, predecesseurs, x, y, directions); // Explorer les voisins
            } else {
                timeline.stop(); // Arrêter si plus rien à explorer
            }
        }));

        timeline.play(); // Lancer l'animation
    }

    /**
     * Anime la résolution du labyrinthe avec l'algorithme DFS.
     *
     * @param delai           le délai entre chaque étape en millisecondes
     * @param tempsLabel      le label pour afficher le temps d'exécution
     * @param etapesLabel     le label pour afficher le nombre d'étapes
     * @param cellulesLabel   le label pour afficher le nombre de cellules explorées
     * @param comparaisonLabel le label pour afficher la comparaison BFS/DFS
     */
    public void animerDFS(int delai, Label tempsLabel, Label etapesLabel, Label cellulesLabel, Label comparaisonLabel) {
        renderer.reinitialiserAffichage(); // Réinitialiser l'affichage graphique

        char[][] grid = labyrinthe.getGrid();
        int startX = labyrinthe.getStartX();
        int startY = labyrinthe.getStartY();
        int endX = labyrinthe.getEndX();
        int endY = labyrinthe.getEndY();

        Stack<Integer[]> stack = new Stack<>(); // Pile pour DFS
        Map<String, Integer[]> predecesseurs = new HashMap<>(); // Suivi des prédécesseurs
        boolean[][] visite = new boolean[grid.length][grid[0].length];

        stack.push(new Integer[]{startX, startY});
        visite[startX][startY] = true;

        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}}; // Directions : haut, bas, gauche, droite

        Timeline timeline = new Timeline();
        timeline.setCycleCount(Timeline.INDEFINITE);

        long startTime = System.currentTimeMillis();
        nombreEtapesDFS = 0;
        cellulesExploreesDFS = 1; // Compter la cellule de départ

        timeline.getKeyFrames().add(new KeyFrame(Duration.millis(delai), event -> {
            if (!stack.isEmpty()) {
                Integer[] current = stack.pop(); // Récupérer la prochaine cellule
                int x = current[0];
                int y = current[1];

                renderer.mettreAJourCase(x, y, "explore"); // Marquer comme explorée
                nombreEtapesDFS++;

                if (x == endX && y == endY) { // Si la sortie est atteinte
                    timeline.stop();
                    List<Integer[]> chemin = reconstruireChemin(predecesseurs, endX, endY);
                    afficherChemin(chemin); // Afficher le chemin final
                    jouerSon(); // Jouer le son de succès

                    tempsExecutionDFS = System.currentTimeMillis() - startTime;
                    mettreAJourLabels(tempsLabel, etapesLabel, cellulesLabel, "DFS"); // Mettre à jour les stats
                    updateComparaison(comparaisonLabel); // Mettre à jour la comparaison
                    return;
                }

                explorerVoisins(stack, visite, predecesseurs, x, y, directions); // Explorer les voisins
            } else {
                timeline.stop(); // Arrêter si plus rien à explorer
            }
        }));

        timeline.play(); // Lancer l'animation
    }

    /**
     * Explore les voisins d'une cellule et les ajoute à la collection (file ou pile).
     *
     * @param collection    la collection pour ajouter les voisins (Queue pour BFS, Stack pour DFS)
     * @param visite        le tableau des cellules visitées
     * @param predecesseurs la map des prédécesseurs pour reconstruire le chemin
     * @param x             la coordonnée X actuelle
     * @param y             la coordonnée Y actuelle
     * @param directions    les directions possibles (haut, bas, gauche, droite)
     */
    private void explorerVoisins(Collection<Integer[]> collection, boolean[][] visite,
                                 Map<String, Integer[]> predecesseurs, int x, int y, int[][] directions) {
        char[][] grid = labyrinthe.getGrid();
        for (int[] dir : directions) {
            int nx = x + dir[0];
            int ny = y + dir[1];
            // Vérifier si le voisin est dans les limites, non visité et non un mur
            if (nx >= 0 && nx < grid.length && ny >= 0 && ny < grid[0].length
                    && !visite[nx][ny] && grid[nx][ny] != '#') {
                visite[nx][ny] = true;
                Integer[] next = new Integer[]{nx, ny};
                collection.add(next);
                predecesseurs.put(nx + "," + ny, new Integer[]{x, y}); // Enregistrer le prédécesseur
                if (collection instanceof Queue) {
                    cellulesExploreesBFS++; // Incrémenter pour BFS
                } else {
                    cellulesExploreesDFS++; // Incrémenter pour DFS
                }
            }
        }
    }

    /**
     * Met à jour les labels avec les statistiques de l'algorithme spécifié.
     *
     * @param tempsLabel    le label pour le temps d'exécution
     * @param etapesLabel   le label pour le nombre d'étapes
     * @param cellulesLabel le label pour le nombre de cellules explorées
     * @param algo          l'algorithme utilisé ("BFS" ou "DFS")
     */
    private void mettreAJourLabels(Label tempsLabel, Label etapesLabel, Label cellulesLabel, String algo) {
        if (algo.equals("BFS")) {
            tempsLabel.setText("Temps BFS : " + tempsExecutionBFS + " ms");
            etapesLabel.setText("Étapes BFS : " + nombreEtapesBFS);
            cellulesLabel.setText("Cellules BFS : " + cellulesExploreesBFS);
        } else {
            tempsLabel.setText("Temps DFS : " + tempsExecutionDFS + " ms");
            etapesLabel.setText("Étapes DFS : " + nombreEtapesDFS);
            cellulesLabel.setText("Cellules DFS : " + cellulesExploreesDFS);
        }
    }

    /**
     * Affiche le chemin final sur le renderer.
     *
     * @param chemin la liste des coordonnées du chemin
     */
    private void afficherChemin(List<Integer[]> chemin) {
        for (Integer[] coord : chemin) {
            renderer.mettreAJourCase(coord[0], coord[1], "chemin"); // Marquer chaque cellule du chemin
        }
    }

    /**
     * Joue le son de succès si un MediaPlayer est disponible.
     */
    private void jouerSon() {
        if (mediaPlayer != null) {
            mediaPlayer.stop(); // Arrêter le son actuel s'il joue
            mediaPlayer.play(); // Jouer le son de succès
        }
    }

    /**
     * Résout le labyrinthe avec l'algorithme BFS sans animation.
     *
     * @param labyrinthe le labyrinthe à résoudre
     * @return la liste des coordonnées du chemin trouvé, ou une liste vide si aucun chemin
     */
    public List<Integer[]> resoudreBFS(Labyrinthe labyrinthe) {
        cellulesExploreesBFS = 0;
        char[][] grid = labyrinthe.getGrid();
        int startX = labyrinthe.getStartX();
        int startY = labyrinthe.getStartY();
        int endX = labyrinthe.getEndX();
        int endY = labyrinthe.getEndY();

        Queue<Integer[]> queue = new LinkedList<>();
        Map<String, Integer[]> predecesseurs = new HashMap<>();
        boolean[][] visite = new boolean[grid.length][grid[0].length];

        queue.add(new Integer[]{startX, startY});
        visite[startX][startY] = true;
        cellulesExploreesBFS++;

        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};

        long startTime = System.currentTimeMillis();
        nombreEtapesBFS = 0;

        while (!queue.isEmpty()) {
            Integer[] current = queue.poll();
            int x = current[0];
            int y = current[1];
            nombreEtapesBFS++;

            if (x == endX && y == endY) {
                tempsExecutionBFS = System.currentTimeMillis() - startTime;
                return reconstruireChemin(predecesseurs, endX, endY);
            }

            explorerVoisins(queue, visite, predecesseurs, x, y, directions);
        }
        tempsExecutionBFS = System.currentTimeMillis() - startTime;
        return Collections.emptyList(); // Aucun chemin trouvé
    }

    /**
     * Résout le labyrinthe avec l'algorithme DFS sans animation.
     *
     * @param labyrinthe le labyrinthe à résoudre
     * @return la liste des coordonnées du chemin trouvé, ou une liste vide si aucun chemin
     */
    public List<Integer[]> resoudreDFS(Labyrinthe labyrinthe) {
        cellulesExploreesDFS = 0;
        char[][] grid = labyrinthe.getGrid();
        int startX = labyrinthe.getStartX();
        int startY = labyrinthe.getStartY();
        int endX = labyrinthe.getEndX();
        int endY = labyrinthe.getEndY();

        Stack<Integer[]> stack = new Stack<>();
        Map<String, Integer[]> predecesseurs = new HashMap<>();
        boolean[][] visite = new boolean[grid.length][grid[0].length];

        stack.push(new Integer[]{startX, startY});
        visite[startX][startY] = true;
        cellulesExploreesDFS++;

        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};

        long startTime = System.currentTimeMillis();
        nombreEtapesDFS = 0;

        while (!stack.isEmpty()) {
            Integer[] current = stack.pop();
            int x = current[0];
            int y = current[1];
            nombreEtapesDFS++;

            if (x == endX && y == endY) {
                tempsExecutionDFS = System.currentTimeMillis() - startTime;
                return reconstruireChemin(predecesseurs, endX, endY);
            }

            explorerVoisins(stack, visite, predecesseurs, x, y, directions);
        }
        tempsExecutionDFS = System.currentTimeMillis() - startTime;
        return Collections.emptyList(); // Aucun chemin trouvé
    }

    /**
     * Reconstruit le chemin à partir des prédécesseurs, de la sortie au départ.
     *
     * @param predecesseurs la map des prédécesseurs
     * @param endX          la coordonnée X de la sortie
     * @param endY          la coordonnée Y de la sortie
     * @return la liste des coordonnées du chemin
     */
    private List<Integer[]> reconstruireChemin(Map<String, Integer[]> predecesseurs, int endX, int endY) {
        List<Integer[]> chemin = new ArrayList<>();
        Integer[] current = new Integer[]{endX, endY};
        chemin.add(current);

        while (predecesseurs.containsKey(current[0] + "," + current[1])) {
            current = predecesseurs.get(current[0] + "," + current[1]);
            chemin.add(current); // Ajouter chaque étape du chemin
        }
        Collections.reverse(chemin); // Inverser pour obtenir départ -> sortie
        return chemin;
    }

    /**
     * Met à jour le label de comparaison entre BFS et DFS.
     *
     * @param comparaisonLabel le label à mettre à jour
     */
    private void updateComparaison(Label comparaisonLabel) {
        if (tempsExecutionBFS > 0 && tempsExecutionDFS > 0) {
            String comparaison;
            if (tempsExecutionBFS < tempsExecutionDFS) {
                comparaison = "BFS plus rapide (" + tempsExecutionBFS + " ms vs " + tempsExecutionDFS + " ms)";
            } else if (tempsExecutionBFS > tempsExecutionDFS) {
                comparaison = "DFS plus rapide (" + tempsExecutionDFS + " ms vs " + tempsExecutionBFS + " ms)";
            } else {
                comparaison = "BFS et DFS égaux (" + tempsExecutionBFS + " ms)";
            }
            comparaisonLabel.setText("Comparaison : " + comparaison);
        } else if (tempsExecutionBFS > 0) {
            comparaisonLabel.setText("Comparaison : BFS " + tempsExecutionBFS + " ms (DFS non exécuté)");
        } else if (tempsExecutionDFS > 0) {
            comparaisonLabel.setText("Comparaison : DFS " + tempsExecutionDFS + " ms (BFS non exécuté)");
        }
    }

    /**
     * Retourne le temps d'exécution de BFS.
     *
     * @return le temps en millisecondes
     */
    public long getTempsExecutionBFS() { return tempsExecutionBFS; }

    /**
     * Retourne le nombre d'étapes effectuées par BFS.
     *
     * @return le nombre d'étapes
     */
    public int getNombreEtapesBFS() { return nombreEtapesBFS; }

    /**
     * Retourne le nombre de cellules explorées par BFS.
     *
     * @return le nombre de cellules
     */
    public int getCellulesExploreesBFS() { return cellulesExploreesBFS; }

    /**
     * Retourne le temps d'exécution de DFS.
     *
     * @return le temps en millisecondes
     */
    public long getTempsExecutionDFS() { return tempsExecutionDFS; }

    /**
     * Retourne le nombre d'étapes effectuées par DFS.
     *
     * @return le nombre d'étapes
     */
    public int getNombreEtapesDFS() { return nombreEtapesDFS; }

    /**
     * Retourne le nombre de cellules explorées par DFS.
     *
     * @return le nombre de cellules
     */
    public int getCellulesExploreesDFS() { return cellulesExploreesDFS; }
}

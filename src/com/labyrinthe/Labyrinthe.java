package com.labyrinthe;

import java.util.Random;
import java.util.ArrayList;
import java.util.List;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Représente un labyrinthe avec une grille, un point de départ (S) et une sortie (E).
 * Permet la génération aléatoire ou le chargement depuis un fichier ou un flux.
 */
public class Labyrinthe {
    private char[][] grid; // Matrice pour stocker le labyrinthe
    private int startX, startY; // Coordonnées de départ (S)
    private int endX, endY; // Coordonnées de sortie (E)
    private Random random = new Random(); // Générateur aléatoire pour la création du labyrinthe
    private List<int[]> murs = new ArrayList<>(); // Liste des murs pour l'algorithme de Prim

    /**
     * Construit un labyrinthe généré aléatoirement avec les dimensions spécifiées.
     *
     * @param hauteur la hauteur du labyrinthe
     * @param largeur la largeur du labyrinthe
     */
    public Labyrinthe(int hauteur, int largeur) {
        grid = new char[hauteur][largeur];
        initialiserLabyrinthe();
        genererLabyrinthe();
    }

    /**
     * Construit un labyrinthe à partir d'un fichier spécifié par son chemin.
     *
     * @param cheminFichier le chemin vers le fichier contenant le labyrinthe
     * @throws IOException si le fichier est introuvable, vide ou mal formé
     */
    public Labyrinthe(String cheminFichier) throws IOException {
        List<String> lignes = new ArrayList<>();
        // Lecture du fichier ligne par ligne avec gestion automatique des ressources
        try (BufferedReader lecteur = new BufferedReader(new FileReader(cheminFichier))) {
            String ligne;
            while ((ligne = lecteur.readLine()) != null) {
                if (!ligne.trim().isEmpty()) { // Ignorer les lignes vides
                    lignes.add(ligne);
                }
            }
        }

        // Vérification que le fichier contient des données
        if (lignes.isEmpty()) {
            throw new IOException("Le fichier est vide ou mal formé.");
        }

        int hauteur = lignes.size();
        int largeur = lignes.get(0).length();

        // Validation des dimensions fixes attendues (19x10)
        if (hauteur != 10 || largeur != 19) {
            throw new IOException("Dimensions du labyrinthe invalides. Attendu : 19x10, Trouvé : " + largeur + "x" + hauteur);
        }

        // Vérification de la cohérence des longueurs de lignes
        for (String ligne : lignes) {
            if (ligne.length() != largeur) {
                throw new IOException("Toutes les lignes doivent avoir la même longueur (19 caractères).");
            }
        }

        // Initialisation de la grille et remplissage à partir des lignes lues
        grid = new char[hauteur][largeur];
        for (int i = 0; i < hauteur; i++) {
            String ligne = lignes.get(i);
            for (int j = 0; j < largeur; j++) {
                grid[i][j] = ligne.charAt(j);
                if (grid[i][j] == 'S') {
                    startX = i;
                    startY = j;
                } else if (grid[i][j] == 'E') {
                    endX = i;
                    endY = j;
                }
            }
        }
    }

    /**
     * Construit un labyrinthe à partir d'un flux d'entrée (ressource).
     *
     * @param inputStream le flux contenant les données du labyrinthe
     * @throws IOException si le flux est vide ou mal formé
     */
    public Labyrinthe(InputStream inputStream) throws IOException {
        List<String> lignes = new ArrayList<>();
        // Lecture du flux avec gestion automatique des ressources
        try (BufferedReader lecteur = new BufferedReader(new InputStreamReader(inputStream))) {
            String ligne;
            while ((ligne = lecteur.readLine()) != null) {
                if (!ligne.trim().isEmpty()) { // Ignorer les lignes vides
                    lignes.add(ligne);
                }
            }
        }

        // Vérification que le flux contient des données
        if (lignes.isEmpty()) {
            throw new IOException("Le fichier est vide ou mal formé.");
        }

        int hauteur = lignes.size();
        int largeur = lignes.get(0).length();

        // Validation des dimensions fixes attendues (19x10)
        if (hauteur != 10 || largeur != 19) {
            throw new IOException("Dimensions du labyrinthe invalides. Attendu : 19x10, Trouvé : " + largeur + "x" + hauteur);
        }

        // Vérification de la cohérence des longueurs de lignes
        for (String ligne : lignes) {
            if (ligne.length() != largeur) {
                throw new IOException("Toutes les lignes doivent avoir la même longueur (19 caractères).");
            }
        }

        // Initialisation de la grille et remplissage à partir des lignes lues
        grid = new char[hauteur][largeur];
        for (int i = 0; i < hauteur; i++) {
            String ligne = lignes.get(i);
            for (int j = 0; j < largeur; j++) {
                grid[i][j] = ligne.charAt(j);
                if (grid[i][j] == 'S') {
                    startX = i;
                    startY = j;
                } else if (grid[i][j] == 'E') {
                    endX = i;
                    endY = j;
                }
            }
        }
    }

    /**
     * Retourne la coordonnée X du point de départ.
     *
     * @return la coordonnée X de départ
     */
    public int getStartX() { return startX; }

    /**
     * Retourne la coordonnée Y du point de départ.
     *
     * @return la coordonnée Y de départ
     */
    public int getStartY() { return startY; }

    /**
     * Retourne la coordonnée X du point de sortie.
     *
     * @return la coordonnée X de sortie
     */
    public int getEndX() { return endX; }

    /**
     * Retourne la coordonnée Y du point de sortie.
     *
     * @return la coordonnée Y de sortie
     */
    public int getEndY() { return endY; }

    /**
     * Retourne la largeur du labyrinthe.
     *
     * @return la largeur (nombre de colonnes)
     */
    public int getWidth() { return grid[0].length; }

    /**
     * Retourne la hauteur du labyrinthe.
     *
     * @return la hauteur (nombre de lignes)
     */
    public int getHeight() { return grid.length; }

    /**
     * Initialise le labyrinthe en remplissant la grille de murs ('#').
     */
    private void initialiserLabyrinthe() {
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                grid[i][j] = '#'; // Tous les emplacements sont des murs par défaut
            }
        }
    }

    /**
     * Retourne la grille du labyrinthe.
     *
     * @return la matrice représentant le labyrinthe
     */
    public char[][] getGrid() {
        return grid;
    }

    /**
     * Génère un labyrinthe aléatoire à l'aide de l'algorithme de Prim.
     */
    private void genererLabyrinthe() {
        startX = 1;
        startY = 1;
        grid[startX][startY] = ' '; // Point de départ initial comme chemin
        ajouterMursAdjacents(startX, startY);

        // Boucle principale de l'algorithme de Prim
        while (!murs.isEmpty()) {
            int[] mur = murs.remove(random.nextInt(murs.size())); // Sélection aléatoire d'un mur
            int mx = mur[0];
            int my = mur[1];
            if (peutPercer(mx, my)) {
                grid[mx][my] = ' '; // Percer le mur pour créer un chemin
                ajouterMursAdjacents(mx, my);
            }
        }
        grid[startX][startY] = 'S'; // Marquer le point de départ
        placerSortieAccessible(); // Placer la sortie
    }

    /**
     * Ajoute les murs adjacents à une cellule dans la liste des murs.
     *
     * @param x la coordonnée X de la cellule
     * @param y la coordonnée Y de la cellule
     */
    private void ajouterMursAdjacents(int x, int y) {
        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}}; // Haut, bas, gauche, droite
        for (int[] dir : directions) {
            int nx = x + dir[0];
            int ny = y + dir[1];
            // Vérifier si les coordonnées sont dans les limites et si c'est un mur
            if (nx >= 0 && nx < grid.length && ny >= 0 && ny < grid[0].length && grid[nx][ny] == '#') {
                murs.add(new int[]{nx, ny});
            }
        }
    }

    /**
     * Vérifie si un mur peut être percé (connecté à un seul chemin).
     *
     * @param x la coordonnée X du mur
     * @param y la coordonnée Y du mur
     * @return true si le mur peut être percé, false sinon
     */
    private boolean peutPercer(int x, int y) {
        int espacesAdjacents = 0;
        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
        for (int[] dir : directions) {
            int nx = x + dir[0];
            int ny = y + dir[1];
            // Compter les chemins adjacents dans les limites de la grille
            if (nx >= 0 && nx < grid.length && ny >= 0 && ny < grid[0].length) {
                if (grid[nx][ny] == ' ') {
                    espacesAdjacents++;
                }
            }
        }
        return espacesAdjacents == 1; // Un mur est perable s'il connecte un seul chemin
    }

    /**
     * Place la sortie (E) dans une position accessible du labyrinthe.
     */
    private void placerSortieAccessible() {
        List<int[]> casesAccessibles = new ArrayList<>();
        // Identifier toutes les cases accessibles (chemins hors départ)
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                if (grid[i][j] == ' ' && (i != startX || j != startY)) {
                    casesAccessibles.add(new int[]{i, j});
                }
            }
        }
        if (!casesAccessibles.isEmpty()) {
            // Choisir une position aléatoire parmi les cases accessibles
            int[] pos = casesAccessibles.get(random.nextInt(casesAccessibles.size()));
            endX = pos[0];
            endY = pos[1];
            grid[endX][endY] = 'E';
        } else {
            // Fallback : placer la sortie en bas à droite si aucune case accessible
            endX = grid.length - 2;
            endY = grid[0].length - 2;
            grid[endX][endY] = 'E';
        }
    }

    /**
     * Marque un chemin sur la grille avec des '+' sauf aux points de départ et sortie.
     *
     * @param chemin la liste des coordonnées du chemin
     */
    public void marquerChemin(List<Integer[]> chemin) {
        for (Integer[] coord : chemin) {
            int x = coord[0];
            int y = coord[1];
            if (grid[x][y] != 'S' && grid[x][y] != 'E') {
                grid[x][y] = '+';
            }
        }
    }

    /**
     * Réinitialise les marques '+' en espaces sur la grille.
     */
    public void reinitialiserMarques() {
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                if (grid[i][j] == '+') {
                    grid[i][j] = ' '; // Remplacer les marques par des espaces
                }
            }
        }
    }

    /**
     * Affiche le labyrinthe dans la console avec un espace entre chaque caractère.
     */
    public void afficherLabyrinthe() {
        for (char[] ligne : grid) {
            for (char cellule : ligne) {
                System.out.print(cellule + " "); // Ajout d’un espace pour lisibilité
            }
            System.out.println();
        }
    }
}

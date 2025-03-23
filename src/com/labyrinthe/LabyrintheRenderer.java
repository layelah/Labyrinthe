package com.labyrinthe;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

/**
 * Gère l'affichage graphique d'un labyrinthe en utilisant JavaFX.
 * Permet de rendre la grille, mettre à jour des cellules spécifiques et ajuster la taille des cellules.
 */
public class LabyrintheRenderer {
    private final GridPane gridPane; // Conteneur JavaFX pour afficher la grille
    private final Labyrinthe labyrinthe; // Instance du labyrinthe à afficher
    private final Image murImage; // Image pour les murs
    private final Image departImage; // Image pour le point de départ
    private final Image sortieImage; // Image pour la sortie
    private final Image exploreImage; // Image pour les cellules explorées
    private final Image cheminImage; // Image pour les cellules du chemin solution
    private double cellSize; // Taille actuelle des cellules en pixels

    /**
     * Construit un renderer pour afficher un labyrinthe dans une grille JavaFX.
     *
     * @param gridPane     le conteneur GridPane pour l'affichage
     * @param labyrinthe   le labyrinthe à rendre
     * @param murImage     l'image représentant les murs
     * @param departImage  l'image représentant le point de départ
     * @param sortieImage  l'image représentant la sortie
     * @param exploreImage l'image pour les cellules explorées
     * @param cheminImage  l'image pour les cellules du chemin solution
     * @param cellSize     la taille initiale des cellules en pixels
     */
    public LabyrintheRenderer(GridPane gridPane, Labyrinthe labyrinthe, Image murImage, Image departImage,
                              Image sortieImage, Image exploreImage, Image cheminImage, double cellSize) {
        this.gridPane = gridPane;
        this.labyrinthe = labyrinthe;
        this.murImage = murImage;
        this.departImage = departImage;
        this.sortieImage = sortieImage;
        this.exploreImage = exploreImage;
        this.cheminImage = cheminImage;
        this.cellSize = cellSize;
    }

    /**
     * Affiche l'intégralité du labyrinthe dans le GridPane.
     */
    public void afficherLabyrinthe() {
        gridPane.getChildren().clear(); // Vider le contenu actuel du GridPane
        char[][] grid = labyrinthe.getGrid();
        // Parcourir chaque cellule de la grille pour l'afficher
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                ImageView imageView = createImageView(grid[i][j]);
                gridPane.add(imageView, j, i); // Ajouter l'image à la position (colonne, ligne)
            }
        }
    }

    /**
     * Met à jour l'affichage d'une cellule spécifique avec un type donné.
     *
     * @param x    la coordonnée X (ligne) de la cellule
     * @param y    la coordonnée Y (colonne) de la cellule
     * @param type le type de mise à jour ("explore" ou "chemin")
     */
    public void mettreAJourCase(int x, int y, String type) {
        ImageView imageView;
        // Sélectionner l'image appropriée en fonction du type
        switch (type) {
            case "explore":
                imageView = new ImageView(exploreImage);
                break;
            case "chemin":
                imageView = new ImageView(cheminImage);
                break;
            default:
                return; // Ignorer les types non reconnus
        }
        imageView.setFitWidth(cellSize); // Ajuster la largeur de l'image
        imageView.setFitHeight(cellSize); // Ajuster la hauteur de l'image
        // Supprimer l'ancienne image à cette position, si elle existe
        gridPane.getChildren().removeIf(node -> GridPane.getRowIndex(node) == x && GridPane.getColumnIndex(node) == y);
        gridPane.add(imageView, y, x); // Ajouter la nouvelle image (inversion colonne/ligne pour GridPane)
    }

    /**
     * Réinitialise l'affichage du labyrinthe à son état initial.
     */
    public void reinitialiserAffichage() {
        afficherLabyrinthe(); // Réafficher le labyrinthe tel qu'il est dans l'état actuel
    }

    /**
     * Crée une vue d'image (ImageView) en fonction du caractère de la cellule.
     *
     * @param cell le caractère représentant la cellule ('#', 'S', 'E', ou autre)
     * @return une ImageView configurée pour cette cellule
     */
    private ImageView createImageView(char cell) {
        ImageView imageView;
        // Associer une image au caractère de la cellule
        switch (cell) {
            case '#':
                imageView = new ImageView(murImage);
                break;
            case 'S':
                imageView = new ImageView(departImage);
                break;
            case 'E':
                imageView = new ImageView(sortieImage);
                break;
            default:
                imageView = new ImageView(); // Case vide sans image spécifique
                break;
        }
        imageView.setFitWidth(cellSize); // Définir la largeur de l'image
        imageView.setFitHeight(cellSize); // Définir la hauteur de l'image
        return imageView;
    }

    /**
     * Ajuste dynamiquement la taille des cellules affichées dans le GridPane.
     *
     * @param newCellSize la nouvelle taille des cellules en pixels
     */
    public void ajusterTailleCellules(double newCellSize) {
        this.cellSize = newCellSize;
        // Mettre à jour la taille de toutes les ImageView existantes
        gridPane.getChildren().forEach(node -> {
            if (node instanceof ImageView) {
                ImageView iv = (ImageView) node;
                iv.setFitWidth(cellSize);
                iv.setFitHeight(cellSize);
            }
        });
    }
}

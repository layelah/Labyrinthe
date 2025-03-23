package com.labyrinthe;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.LinearGradient;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.LinearGradient;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

/**
 * Application JavaFX principale pour une interface interactive de résolution de labyrinthes.
 */
public class MainApp extends Application {
    private Labyrinthe labyrinthe; // Instance du labyrinthe affiché
    private GridPane gridPane; // Conteneur pour l'affichage graphique du labyrinthe
    private Solver solver; // Solveur pour les algorithmes BFS et DFS
    private LabyrintheRenderer renderer; // Renderer pour l'affichage graphique
    private Resources resources; // Gestionnaire des ressources (images, sons)

    private Label tempsBFSLabel, etapesBFSLabel, cellulesBFSLabel; // Labels pour les stats BFS
    private Label tempsDFSLabel, etapesDFSLabel, cellulesDFSLabel; // Labels pour les stats DFS
    private Label comparaisonLabel; // Label pour la comparaison BFS/DFS

    private static final int LABYRINTH_WIDTH = 19;  // Largeur fixe du labyrinthe en colonnes
    private static final int LABYRINTH_HEIGHT = 10; // Hauteur fixe du labyrinthe en lignes

    /**
     * Initialise et affiche l'interface graphique de l'application.
     *
     * @param primaryStage la fenêtre principale de l'application
     */
    @Override
    public void start(Stage primaryStage) {
        resources = new Resources(); // Chargement des ressources (images, sons)
        gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER); // Centrer le contenu du GridPane

        // Initialisation des labels pour les statistiques
        tempsBFSLabel = createStatLabel("Temps BFS : -");
        etapesBFSLabel = createStatLabel("Étapes BFS : -");
        cellulesBFSLabel = createStatLabel("Cellules BFS : -");
        tempsDFSLabel = createStatLabel("Temps DFS : -");
        etapesDFSLabel = createStatLabel("Étapes DFS : -");
        cellulesDFSLabel = createStatLabel("Cellules DFS : -");
        comparaisonLabel = createStatLabel("Comparaison : -");

        // Création des boutons stylés
        Button bfsButton = createStyledButton("Résoudre BFS", "#0078D4");
        Button dfsButton = createStyledButton("Résoudre DFS", "#0078D4");
        Button resetButton = createStyledButton("Nouveau", "#FF5722");

        // Configuration du label et du slider pour la vitesse d'animation
        Label vitesseLabel = new Label("Vitesse : 100 ms");
        vitesseLabel.setFont(Font.font("Segoe UI", FontWeight.SEMI_BOLD, 14));
        vitesseLabel.setTextFill(Color.WHITE);
        Slider vitesseSlider = new Slider(50, 500, 100); // Plage de 50 à 500 ms, valeur initiale 100 ms
        vitesseSlider.setShowTickLabels(true);
        vitesseSlider.setShowTickMarks(true);
        vitesseSlider.setMajorTickUnit(50);
        vitesseSlider.setMinorTickCount(5);
        vitesseSlider.setStyle("-fx-background-color: linear-gradient(to right, #0078D4, #00CC6A);");
        vitesseSlider.valueProperty().addListener((obs, oldVal, newVal) ->
                vitesseLabel.setText("Vitesse : " + newVal.intValue() + " ms")); // Mise à jour dynamique du label

        // Mise en page des contrôles (boutons et slider)
        HBox boutonsBox = new HBox(20, bfsButton, dfsButton, resetButton); // Espacement de 20 entre éléments
        boutonsBox.setAlignment(Pos.CENTER);
        boutonsBox.setPadding(new Insets(15));

        VBox controleBox = new VBox(20, boutonsBox, vitesseLabel, vitesseSlider);
        controleBox.setAlignment(Pos.CENTER);
        controleBox.setPadding(new Insets(20));
        controleBox.setBackground(new Background(new BackgroundFill(
                LinearGradient.valueOf("to bottom, #1E1E1E, #2D2D2D"),
                new CornerRadii(15), new Insets(0)))); // Fond dégradé avec coins arrondis

        // Mise en page des statistiques
        GridPane statsGrid = new GridPane();
        statsGrid.setHgap(25); // Espacement horizontal entre colonnes
        statsGrid.setVgap(15); // Espacement vertical entre lignes
        statsGrid.setPadding(new Insets(20));
        statsGrid.setBackground(new Background(new BackgroundFill(
                Color.web("#2D2D2D"), new CornerRadii(15), new Insets(0))));
        statsGrid.setEffect(new DropShadow(10, Color.BLACK)); // Ombre portée pour effet visuel
        statsGrid.addRow(0, tempsBFSLabel, etapesBFSLabel, cellulesBFSLabel);
        statsGrid.addRow(1, tempsDFSLabel, etapesDFSLabel, cellulesDFSLabel);
        statsGrid.add(comparaisonLabel, 0, 2, 3, 1); // Label de comparaison sur 3 colonnes
        statsGrid.setAlignment(Pos.CENTER);

        // Mise en page principale avec BorderPane
        BorderPane root = new BorderPane();
        root.setTop(controleBox); // Contrôles en haut
        root.setCenter(gridPane); // Labyrinthe au centre
        root.setBottom(statsGrid); // Statistiques en bas
        root.setPadding(new Insets(10));
        root.setBackground(new Background(new BackgroundFill(
                LinearGradient.valueOf("to bottom right, #121212, #1E1E1E"),
                CornerRadii.EMPTY, Insets.EMPTY))); // Fond dégradé global

        // Actions des boutons
        bfsButton.setOnAction(e -> solver.animerBFS((int) vitesseSlider.getValue(), tempsBFSLabel, etapesBFSLabel, cellulesBFSLabel, comparaisonLabel));
        dfsButton.setOnAction(e -> solver.animerDFS((int) vitesseSlider.getValue(), tempsDFSLabel, etapesDFSLabel, cellulesDFSLabel, comparaisonLabel));
        resetButton.setOnAction(e -> initialiserLabyrinthe());

        // Configuration de la scène
        Scene scene = new Scene(root, 1000, 850); // Taille initiale de la fenêtre
        scene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm()); // Application du CSS
        primaryStage.setTitle("Labyrinthe Élégant");

        // Bindings pour ajuster dynamiquement la taille du GridPane
        gridPane.prefWidthProperty().bind(root.widthProperty().subtract(20));
        gridPane.prefHeightProperty().bind(root.heightProperty().subtract(controleBox.heightProperty().add(statsGrid.heightProperty()).add(40)));

        // Listeners pour redimensionner le labyrinthe lors des changements de taille de fenêtre
        scene.widthProperty().addListener((obs, oldVal, newVal) -> ajusterLabyrinthe());
        scene.heightProperty().addListener((obs, oldVal, newVal) -> ajusterLabyrinthe());

        // Initialisation initiale du labyrinthe
        initialiserLabyrinthe();

        primaryStage.setScene(scene);
        primaryStage.show(); // Afficher la fenêtre
    }

    /**
     * Crée un bouton stylé avec effet de survol.
     *
     * @param text  le texte du bouton
     * @param color la couleur de fond initiale en hexadécimal
     * @return le bouton configuré
     */
    private Button createStyledButton(String text, String color) {
        Button button = new Button(text);
        button.setFont(Font.font("Segoe UI", FontWeight.BOLD, 14));
        button.setPrefWidth(120);
        button.setStyle(
                "-fx-background-color: " + color + ";" +
                        "-fx-text-fill: white;" +
                        "-fx-background-radius: 10;" +
                        "-fx-padding: 8 15 8 15;" +
                        "-fx-font-size: 14px;"
        );
        button.setEffect(new DropShadow(5, Color.web("#00000080"))); // Ombre portée légère
        // Effet de survol : éclaircir la couleur
        button.setOnMouseEntered(e -> button.setStyle(
                "-fx-background-color: derive(" + color + ", 20%);" +
                        "-fx-text-fill: white;" +
                        "-fx-background-radius: 10;" +
                        "-fx-padding: 8 15 8 15;" +
                        "-fx-font-size: 14px;"
        ));
        // Retour à la couleur initiale hors survol
        button.setOnMouseExited(e -> button.setStyle(
                "-fx-background-color: " + color + ";" +
                        "-fx-text-fill: white;" +
                        "-fx-background-radius: 10;" +
                        "-fx-padding: 8 15 8 15;" +
                        "-fx-font-size: 14px;"
        ));
        return button;
    }

    /**
     * Crée un label pour afficher les statistiques.
     *
     * @param text le texte initial du label
     * @return le label configuré
     */
    private Label createStatLabel(String text) {
        Label label = new Label(text);
        label.setFont(Font.font("Segoe UI", FontWeight.NORMAL, 13));
        label.setTextFill(Color.web("#E0E0E0")); // Couleur claire pour contraste
        return label;
    }

    /**
     * Initialise un nouveau labyrinthe et met à jour l'affichage et le solveur.
     */
    private void initialiserLabyrinthe() {
        labyrinthe = new Labyrinthe(LABYRINTH_HEIGHT, LABYRINTH_WIDTH); // Nouveau labyrinthe aléatoire
        double cellSize = calculateCellSize(); // Calculer la taille des cellules
        renderer = new LabyrintheRenderer(gridPane, labyrinthe, resources.getMurImage(), resources.getDepartImage(),
                resources.getSortieImage(), resources.getExploreImage(), resources.getCheminImage(), cellSize);
        renderer.afficherLabyrinthe(); // Afficher le labyrinthe
        solver = new Solver(labyrinthe, renderer, resources.getMediaPlayer()); // Nouveau solveur
        resetLabels(); // Réinitialiser les labels
    }

    /**
     * Ajuste la taille des cellules du labyrinthe en fonction des dimensions actuelles.
     */
    private void ajusterLabyrinthe() {
        if (renderer != null) {
            renderer.ajusterTailleCellules(calculateCellSize()); // Mettre à jour la taille des cellules
        }
    }

    /**
     * Calcule la taille optimale des cellules en fonction des dimensions du GridPane.
     *
     * @return la taille des cellules en pixels
     */
    private double calculateCellSize() {
        double width = gridPane.getWidth();
        double height = gridPane.getHeight();
        if (width <= 0 || height <= 0) return 20; // Taille par défaut si non calculé
        return Math.min(width / LABYRINTH_WIDTH, height / LABYRINTH_HEIGHT); // Taille adaptée à la grille
    }

    /**
     * Réinitialise tous les labels de statistiques à leur état initial.
     */
    private void resetLabels() {
        tempsBFSLabel.setText("Temps BFS : -");
        etapesBFSLabel.setText("Étapes BFS : -");
        cellulesBFSLabel.setText("Cellules BFS : -");
        tempsDFSLabel.setText("Temps DFS : -");
        etapesDFSLabel.setText("Étapes DFS : -");
        cellulesDFSLabel.setText("Cellules DFS : -");
        comparaisonLabel.setText("Comparaison : -");
    }

    /**
     * Affiche une alerte en cas d'erreur.
     *
     * @param title   le titre de l'alerte
     * @param message le message à afficher
     */
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.getDialogPane().setStyle("-fx-background-color: #2D2D2D; -fx-text-fill: white;"); // Style personnalisé
        alert.showAndWait(); // Attendre la fermeture par l'utilisateur
    }

    /**
     * Point d'entrée pour lancer l'application JavaFX.
     *
     * @param args les arguments de la ligne de commande
     */
    public static void main(String[] args) {
        launch(args); // Démarrer l'application
    }
}

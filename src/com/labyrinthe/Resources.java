package com.labyrinthe;

import javafx.scene.image.Image;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import java.net.URL;

/**
 * Gère le chargement et l'accès aux ressources graphiques et sonores de l'application.
 */
public class Resources {
    private final Image murImage; // Image pour les murs du labyrinthe
    private final Image departImage; // Image pour le point de départ
    private final Image sortieImage; // Image pour la sortie
    private final Image exploreImage; // Image pour les cellules explorées
    private final Image cheminImage; // Image pour les cellules du chemin solution
    private final MediaPlayer mediaPlayer; // Lecteur pour le son de succès

    /**
     * Construit une instance de Resources en chargeant les images et le son depuis le dossier /resources/.
     * Lance une exception si une ressource est introuvable.
     */
    public Resources() {
        // Charger les ressources depuis src/resources/
        murImage = new Image(getClass().getResourceAsStream("/mur.png")); // Image des murs
        departImage = new Image(getClass().getResourceAsStream("/depart.png")); // Image du départ
        sortieImage = new Image(getClass().getResourceAsStream("/sortie.png")); // Image de la sortie
        exploreImage = new Image(getClass().getResourceAsStream("/explore.png")); // Image des cellules explorées
        cheminImage = new Image(getClass().getResourceAsStream("/chemin.png")); // Image du chemin solution

        // Chargement du fichier audio
        URL soundUrl = getClass().getResource("/success.wav");
        if (soundUrl == null) {
            throw new RuntimeException("Fichier son 'success.wav' introuvable dans les ressources.");
        }
        mediaPlayer = new MediaPlayer(new Media(soundUrl.toString())); // Initialisation du lecteur audio
    }

    /**
     * Retourne l'image utilisée pour représenter les murs.
     *
     * @return l'image des murs
     */
    public Image getMurImage() { return murImage; }

    /**
     * Retourne l'image utilisée pour représenter le point de départ.
     *
     * @return l'image du départ
     */
    public Image getDepartImage() { return departImage; }

    /**
     * Retourne l'image utilisée pour représenter la sortie.
     *
     * @return l'image de la sortie
     */
    public Image getSortieImage() { return sortieImage; }

    /**
     * Retourne l'image utilisée pour représenter les cellules explorées.
     *
     * @return l'image des cellules explorées
     */
    public Image getExploreImage() { return exploreImage; }

    /**
     * Retourne l'image utilisée pour représenter les cellules du chemin solution.
     *
     * @return l'image du chemin solution
     */
    public Image getCheminImage() { return cheminImage; }

    /**
     * Retourne le lecteur multimédia pour le son de succès.
     *
     * @return le MediaPlayer configuré
     */
    public MediaPlayer getMediaPlayer() { return mediaPlayer; }
}

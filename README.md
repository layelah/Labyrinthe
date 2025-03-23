# 🎯 Résolution de Labyrinthe

## 🚀 Version : 1.0.0

### 👨‍💻 Développé par :
- **[Abdoulaye LAH]** ([GitHub](https://github.com/layelah))
- **[Ndeye Daba Seck]** ([GitHub](https://github.com/Daba200616))
- **[Aliou Badara Sané]** ([GitHub](https://github.com/Aliou774))

Bienvenue dans **Résolution de Labyrinthe**, une application développée dans le cadre du cours de **Programmation et Algorithmique Avancée** à l’**École Supérieure Polytechnique de Dakar (Master 1 GLSI & SRT)** sous la supervision du **Dr Mouhamed Diop**. Ce projet implémente les algorithmes **BFS** et **DFS** pour résoudre des labyrinthes, avec une interface graphique en bonus pour une expérience interactive. 🔥

---

![zoro](https://github.com/user-attachments/assets/50c4f28d-edc4-4284-9462-60219cf54f94)

---

## 📌 Description de l'Application

### 🌍 Aperçu Général
🔹 **Résolution de Labyrinthe** permet de :  
✅ Générer un labyrinthe  
✅ Trouver un chemin entre le départ (`S`) et la sortie (`E`) avec `BFS` ou `DFS`  
✅ Visualiser la résolution en temps réel

### 🔥 Fonctionnalités Principales
- **🌀 Génération de labyrinthes** : Crée des labyrinthes aléatoires avec `Labyrinthe(int hauteur, int largeur)`.
- **📜 Résolution** :
  - `BFS` (`resoudreBFS`) trouve le chemin le plus court.
  - `DFS` (`resoudreDFS`) trouve un chemin quelconque.
- **🎨 Interface graphique** : Basée sur **JavaFX**, avec animation via `animerBFS` et `animerDFS`.
- **📊 Comparaison des performances** : Temps, étapes, et cellules explorées affichés.
- **🖥️ Mode console** : Test via `main` dans `Main.java`.

---

## ⚙️ Architecture Technique
- **💻 Langage** : Java (version 21, compatible avec Liberica JDK 21 Full)
- **📚 Bibliothèques** : JavaFX, Java Collections (`Queue`, `Stack`)
- **🔗 Structure** : Packages dans `src/com/labyrinthe`
- **📦 Ressources** : Images et CSS dans `src/resources`

---

## 🛠️ Prérequis pour le Déploiement
✅ **Java Development Kit (JDK)** - Version 21 (recommandé : Liberica JDK 21 Full) - [Télécharger](https://bell-sw.com/pages/downloads/)  
✅ **IntelliJ IDEA (optionnel)** - [Télécharger](https://www.jetbrains.com/idea/)  
✅ **Git (optionnel)** - [Télécharger](https://git-scm.com/downloads)

---

## 📥 Guide d'Installation et de Déploiement

### 1️⃣ Installer le JDK
🔹 Téléchargez et installez **Liberica JDK 21 Full** depuis [BellSoft](https://bell-sw.com/pages/downloads/).  
🔹 Vérifiez l’installation avec : `java -version` (vous devriez voir une sortie indiquant JDK 21).  
🔹 **Note** : Si vous utilisez IntelliJ IDEA, configurez le projet pour utiliser ce JDK (voir [Configurer l’IDE](#configurer-lide)).

### 2️⃣ Télécharger le Projet
🔹 Accédez au dépôt [GitHub](https://github.com/tonprofil/layelah-labyrinthe).  
🔹 Cliquez sur **Download ZIP** et extrayez le fichier.

### 3️⃣ Lancer en Mode Console 🚀
🔹 Ouvrez le projet dans votre IDE ou un terminal.  
🔹 Assurez-vous que le projet est compilé avec JDK 21.  
🔹 Exécutez `Main.java` pour voir un labyrinthe généré et résolu avec `BFS` et `DFS` dans la console.

### 4️⃣ Lancer l’Interface Graphique 🎯
🔹 Ouvrez le projet dans IntelliJ IDEA.  
🔹 Assurez-vous que Liberica JDK 21 Full est configuré comme SDK du projet.  
🔹 Exécutez `MainApp.java` pour lancer l’interface graphique avec `start(Stage primaryStage)`.  
✅ Une fenêtre s’ouvre pour interagir avec le labyrinthe.

### 🛠️ Configurer l’IDE (IntelliJ IDEA)
Pour utiliser IntelliJ IDEA avec Liberica JDK 21 Full :
1. Ouvrez **File > Project Structure**.
2. Sous **Platform Settings > SDKs**, cliquez sur **+** et sélectionnez le dossier d’installation de Liberica JDK 21 Full.
3. Sous **Project Settings > Project**, choisissez ce JDK comme **Project SDK**.
4. Dans **Build, Execution, Deployment > Compiler > Java Compiler**, vérifiez que la **version du bytecode cible** est réglée sur 21.

---

## 🖥️ Utilisation de l'Application

### 🎮 Mode Console
🔹 Lancez `Main.java` pour voir un labyrinthe généré et résolu textuellement dans la console.

### 📊 Mode Graphique
🔹 **Nouveau** : Cliquez pour générer un labyrinthe avec `initialiserLabyrinthe()`.  
🔹 **Résoudre BFS/DFS** : Lancez `animerBFS` ou `animerDFS` pour visualiser le chemin.  
🔹 **Stats** : Temps et étapes sont affichés en bas de l’interface.

---

## ⚠️ Remarques Importantes
❗ **Taille du labyrinthe** : 19x10 par défaut pour les fichiers chargés.  
❗ **Images** : Vérifiez que `src/resources` contient `mur.png`, etc.  
❗ **Délai** : Ajustable avec le slider dans l’interface graphique.  
❗ **Compatibilité JDK** : Utilisez JDK 21 (de préférence Liberica JDK 21 Full) pour éviter des problèmes de compatibilité.

---

## 📞 Contact
Pour toute question ou suggestion, n’hésitez pas à nous contacter :

- **[Abdoulaye LAH]** : ([GitHub](https://github.com/layelah))
- **[Ndeye Daba Seck]** : ([GitHub](https://github.com/Daba200616))
- **[Aliou Badara Sané]** : ([GitHub](https://github.com/Aliou774))

🚀 Merci d’explorer **Résolution de Labyrinthe** ! 🎯

# 🌐 SEARC#

## 👥 Équipe 40
🔗 [Fichiers sources](https://github.com/Tyvain/noelmusk/tree/equipe40)

## 🧑‍💻 Membres
- **John WAIA** 
- **Jo-Michel UTO**
- **Keys DIANOU**
- **Malcolm BERTAINA**
- **Maxime TONNELIER**
- **Morgan CARRE**
- **Romain CASTELAIN**

## 🎯 Objectifs initiaux
- Permettre la recherche de publications sur différents réseaux sociaux à partir de hashtags ou de mots-clés.  
- Intégrer une nouvelle API sociale en complément de celle déjà existante pour Mastodon.  
- Repenser l'interface utilisateur et l'expérience utilisateur afin de rendre l'application entièrement navigable au clavier.

## 🚀 Fonctionnalités développées

### ✅ Version 1
- Structuration du code de base du projet.
- Ajout des premières commandes de navigation : `Goto`, `Top`, `Bottom`, `Next`, `Previous`, `Play`.
- Implémentation de fonctionnalités diverses : `Link`, `RepliesGreater`, `SortReplies`, `Help`, etc.
- Optimisation des appels à l’API Mastodon.
- Désactivation du clic et du scroll sur la page
- Affichage des posts dans une grille interactive.
- Système de notifications utilisateur.

### 🚧 Version 2
- Amélioration des commandes `Top` et `Bottom` pour une navigation plus fluide.
- Enrichissement de la commande `Help` pour une meilleure lisibilité.
- Refonte partielle de l’interface utilisateur.
- Affichage de données supplémentaires pour chaque post (likes, réponses, reposts, dates, etc.).
- Ajout de la commande `History` pour naviguer dans l’historique de consultation.

### 🔬 Version 3
- Ajout du scroll automatique lors de la sélection d’un post en dehors de la zone visible.
- Correction de l’affichage de la grille des résultats.
- Intégration de l’API Reddit en complément de Mastodon.
- Affichage d’un badge indiquant la plateforme d’origine du post (Mastodon ou Reddit).
- Refactorisation de la commande `RepliesGreater` en `Filter`, plus générique.
- Ajout de la commande `Open` permettant d’ouvrir un post dans un nouvel onglet.
- Navigation dans l’historique désormais possible via le clavier.
- Ajout d’un écran d’accueil listant l’ensemble des commandes disponibles à l’ouverture du site.


## 🗂️ Structure du projet
- `components` : composants UI réutilisables (ex : cartes de commandes, badges de plateforme).
- `factory` : classes de fabrique pour instancier les services et commandes sociales.
- `model` : classes de données représentant les entités du projet (posts, comptes, tags, etc.).
- `service` : services dédiés à la récupération et au traitement des données depuis les réseaux sociaux.
- `utils` : fonctions utilitaires pour la gestion de la Grid et des notifications.
- `views.main` : vues principales de l’application côté serveur.
- `views.main.commands` : commandes spécifiques accessibles via l’interface (ex : navigation, tri, recherche, etc.).

## ⚙️ Lancer le projet

### Prérequis
- Java 17+
- Maven 3.6+
- Navigateur web moderne

### Lancement
```bash
git clone https://github.com/morgancarre/noelmusk.git
cd noelmusk
mvn spring-boot:run
```

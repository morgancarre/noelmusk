package antix.views.main;

import antix.model.MastodonPost;

// Interface fonctionnelle, elle permet de passer une méthode et d'affichage personnalisée

@FunctionalInterface
public interface PostSelector {
    void selectAndDisplay(MastodonPost post);
}

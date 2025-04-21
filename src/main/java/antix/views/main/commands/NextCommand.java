package antix.views.main.commands;

import antix.model.MastodonPost;
import antix.views.main.PostSelector;

import com.vaadin.flow.component.grid.Grid;

import java.util.List;

/**
 * Commande permettant de sélectionner le post suivant dans la liste.
 * Si aucun post n'est sélectionné, elle prend le premier.
 */
public class NextCommand extends NavigationCommand {

    /**
     * Constructeur de la commande Next.
     *
     * @param grid     Grille affichant les posts.
     * @param selector Sélecteur de post pour affichage.
     */
    public NextCommand(Grid<MastodonPost> grid, PostSelector selector) {
        super("Next", "n / next : sélectionner le post suivant", grid, selector);
    }

    /**
     * Retourne une description de la commande.
     *
     * @return Texte descriptif pour la commande d'aide.
     */
    @Override
    public String getDescription() {
        return "n / next : sélectionner le post suivant";
    }

    /**
     * Renvoie le post suivant dans la liste.
     *
     * @param items   Liste complète des posts affichés.
     * @param current Le post actuellement sélectionné.
     * @return Le post suivant, ou le premier si aucun n'est sélectionné.
     */
    @Override
    protected MastodonPost getTargetPost(List<MastodonPost> items, MastodonPost current) {
        int index = (current != null) ? items.indexOf(current) : -1;
        return (index >= 0 && index < items.size() - 1)
                ? items.get(index + 1)
                : (!items.isEmpty() ? items.get(0) : null);
    }
}

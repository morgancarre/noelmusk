package antix.views.main.commands;

import antix.model.MastodonPost;
import antix.views.main.PostSelector;

import com.vaadin.flow.component.grid.Grid;

import java.util.List;

/**
 * Commande permettant de sélectionner le post précédent dans la liste.
 * Si aucun post n'est sélectionné, elle prend le dernier.
 */
public class PreviousCommand extends NavigationCommand {

    /**
     * Constructeur de la commande Previous.
     *
     * @param grid     Grille contenant les posts.
     * @param selector Sélecteur de post pour affichage.
     */
    public PreviousCommand(Grid<MastodonPost> grid, PostSelector selector) {
        super(List.of("p", "previous"), "Previous", "p / previous : sélectionner le post précédent", grid, selector);
    }

    /**
     * Renvoie le post précédent dans la liste.
     *
     * @param items   Liste actuelle des posts.
     * @param current Le post sélectionné actuellement.
     * @return Le post précédent ou le dernier si aucun n’est sélectionné.
     */
    @Override
    protected MastodonPost getTargetPost(List<MastodonPost> items, MastodonPost current) {
        int index = (current != null) ? items.indexOf(current) : items.size();
        return (index > 0)
                ? items.get(index - 1)
                : (!items.isEmpty() ? items.get(items.size() - 1) : null);
    }
}

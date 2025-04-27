package antix.views.main.commands;

import antix.model.MastodonPost;
import antix.views.main.PostSelector;
import com.vaadin.flow.component.grid.Grid;
import java.util.List;

/**
 * Commande permettant de sélectionner le dernier post de la page.
 * Elle sélectionne simplement le dernier élément de la liste visible.
 */
public class BottomCommand extends NavigationCommand {
    /**
     * Constructeur de la commande Bottom.
     *
     * @param grid     Grille contenant les posts à afficher.
     * @param selector Interface permettant d'afficher un post dans l'UI.
     */
    public BottomCommand(Grid<MastodonPost> grid, PostSelector selector) {
        super(List.of("bottom"), "Bottom", "bottom : sélectionne le dernier post de la page", grid, selector);
    }

    /**
     * Sélectionne le dernier post de la page.
     *
     * @param items   Liste complète des posts affichés.
     * @param current Le post actuellement sélectionné (non utilisé ici).
     * @return Le dernier post de la page, ou null si la liste est vide.
     */
    @Override
    protected MastodonPost getTargetPost(List<MastodonPost> items, MastodonPost current) {
        return items.isEmpty() ? null : items.get(items.size() - 1);
    }
}
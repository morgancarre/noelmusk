package antix.views.main.commands;

import antix.model.MastodonPost;
import antix.views.main.PostSelector;

import com.vaadin.flow.component.grid.Grid;

import java.util.Comparator;
import java.util.List;

/**
 * Commande permettant de sélectionner le post ayant le moins de réponses.
 * Elle trie les posts par nombre de réponses croissant et sélectionne le
 * premier.
 */
public class BottomCommand extends NavigationCommand {

    /**
     * Constructeur de la commande Bottom.
     *
     * @param grid     Grille contenant les posts à afficher.
     * @param selector Interface permettant d'afficher un post dans l'UI.
     */
    public BottomCommand(Grid<MastodonPost> grid, PostSelector selector) {
        super(List.of("bottom"), "Bottom", "Sélectionne le post avec le moins de réponses.", grid, selector);
    }

    /**
     * Sélectionne le post ayant le plus petit nombre de réponses.
     *
     * @param items   Liste complète des posts affichés.
     * @param current Le post actuellement sélectionné (non utilisé ici).
     * @return Le post avec le moins de réponses, ou null si la liste est vide.
     */
    @Override
    protected MastodonPost getTargetPost(List<MastodonPost> items, MastodonPost current) {
        return items.stream()
                .min(Comparator.comparingInt(MastodonPost::getRepliesCount))
                .orElse(null);
    }
}

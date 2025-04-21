package antix.views.main.commands;

import antix.model.MastodonPost;
import antix.views.main.PostSelector;

import com.vaadin.flow.component.grid.Grid;

import java.util.Comparator;
import java.util.List;

/**
 * Commande permettant de sélectionner le post ayant le plus grand nombre de
 * réponses.
 * Elle trie les éléments par ordre décroissant de réponses et sélectionne le
 * premier.
 */
public class TopCommand extends NavigationCommand {

    /**
     * Constructeur.
     *
     * @param grid     La grille contenant les posts affichés.
     * @param selector Le sélecteur responsable de l'affichage du post choisi.
     */
    public TopCommand(Grid<MastodonPost> grid, PostSelector selector) {
        super("Top", "top : sélectionne le post avec le plus de réponses", grid, selector);
    }

    /**
     * Définit la logique de sélection du post à afficher.
     *
     * @param items   La liste de posts visibles.
     * @param current Le post actuellement sélectionné (peut être null).
     * @return Le post avec le plus de réponses.
     */
    @Override
    protected MastodonPost getTargetPost(List<MastodonPost> items, MastodonPost current) {
        return items.stream()
                .max(Comparator.comparingInt(MastodonPost::getRepliesCount))
                .orElse(null);
    }
}

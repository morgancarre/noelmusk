package antix.views.main.commands;

import antix.model.MastodonPost;
import antix.utils.GridUtils;
import antix.views.main.PostSelector;

import com.vaadin.flow.component.grid.Grid;

import java.util.List;

/**
 * Commande qui trie les posts par nombre de réponses décroissant.
 * Le post ayant le plus de réponses est sélectionné automatiquement.
 */
public class SortRepliesCommand extends Command {
    private final Grid<MastodonPost> grid;
    private final PostSelector selector;

    /**
     * Constructeur.
     *
     * @param grid     Grille d'affichage.
     * @param selector Permet d'afficher le post après tri.
     */
    public SortRepliesCommand(Grid<MastodonPost> grid, PostSelector selector) {
        super(List.of("sort replies"), "Sort Replies", "sort replies : trie les posts par nombre de réponses décroissant");
        this.grid = grid;
        this.selector = selector;
    }

    /**
     * Trie les posts par nombre de réponses décroissant et sélectionne le premier.
     *
     * @param input Non utilisé ici.
     */
    @Override
    public void execute(String input) {
        List<MastodonPost> items = GridUtils.fetchAll(grid);
        items.sort((a, b) -> Integer.compare(b.getRepliesCount(), a.getRepliesCount()));

        grid.setItems(items);

        if (!items.isEmpty()) {
            selector.selectAndDisplay(items.get(0));
        }
    }
}

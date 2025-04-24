package antix.views.main.commands;

import antix.model.MastodonPost;
import antix.utils.FeedbackUtils;
import antix.utils.GridUtils;
import antix.views.main.PostSelector;

import com.vaadin.flow.component.grid.Grid;

import java.util.List;

/**
 * Commande permettant d'aller directement au post numéro n dans la liste
 * affichée.
 * L'index est 1-based (commence à 1 pour l'utilisateur).
 */
public class GotoCommand extends Command {
    private final Grid<MastodonPost> grid;
    private final PostSelector selector;

    /**
     * Constructeur de la commande Goto.
     *
     * @param grid        Grille des posts.
     * @param selector    Sélecteur de post pour affichage.
     */
    public GotoCommand(Grid<MastodonPost> grid, PostSelector selector) {
        super(List.of("g", "goto"),"Goto", "Aller au post numéro n");
        this.grid = grid;
        this.selector = selector;
    }

    /**
     * Exécute la commande avec l’index donné par l’utilisateur.
     *
     * @param input Entrée utilisateur, ex : "goto 3"
     */
    @Override
    public void execute(String input) {
        String[] parts = input.trim().split("\\s+");
        if (parts.length < 2) {
            FeedbackUtils.showError("Veuillez spécifier un index (ex: goto 3).");
            return;
        }

        int index;
        try {
            index = Integer.parseInt(parts[1]) - 1;
        } catch (NumberFormatException e) {
            FeedbackUtils.showError("Index invalide : '" + parts[1] + "'.");
            return;
        }

        List<MastodonPost> posts = GridUtils.fetchAll(grid);
        if (index >= 0 && index < posts.size()) {
            selector.selectAndDisplay(posts.get(index));
            FeedbackUtils.showSuccess("Post numéro " + (index + 1) + " sélectionné.");
        } else {
            FeedbackUtils.showError("Index hors limites. Il doit être entre 1 et " + posts.size() + ".");
        }
    }
}

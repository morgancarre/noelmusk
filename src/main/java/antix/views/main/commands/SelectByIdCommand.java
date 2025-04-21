package antix.views.main.commands;

import antix.model.MastodonPost;
import antix.utils.FeedbackUtils;
import antix.utils.GridUtils;
import antix.views.main.PostSelector;

import com.vaadin.flow.component.grid.Grid;

import java.util.List;

/**
 * Commande permettant de sélectionner un post via son identifiant (ID exact).
 * Utile pour une recherche précise.
 */
public class SelectByIdCommand extends Command {
    private final Grid<MastodonPost> grid;
    private final PostSelector selector;

    /**
     * Constructeur de la commande SelectById.
     *
     * @param grid       Grille contenant les posts.
     * @param selector   Interface de sélection/affichage.
     */
    public SelectByIdCommand(Grid<MastodonPost> grid, PostSelector selector) {
        super("Select", "select <id> : sélectionner un post via son identifiant unique");
        this.grid = grid;
        this.selector = selector;
    }

    /**
     * Exécute la commande en recherchant l’ID fourni.
     *
     * @param input Entrée utilisateur, ex : "select 109384881"
     */
    @Override
    public void execute(String input) {
        String id = input.replaceFirst("select\\s+", "").trim();
        if (id.isEmpty()) {
            FeedbackUtils.showError("Veuillez spécifier un ID valide (ex: select 109384881).");
            return;
        }

        List<MastodonPost> items = GridUtils.fetchAll(grid);
        boolean found = items.stream()
                .filter(post -> String.valueOf(post.getId()).equals(id))
                .findFirst()
                .map(post -> {
                    selector.selectAndDisplay(post);
                    FeedbackUtils.showSuccess("Post ID " + id + " sélectionné.");
                    return true;
                }).orElse(false);

        if (!found) {
            FeedbackUtils.showError("Aucun post trouvé avec l'ID : " + id);
        }
    }
}

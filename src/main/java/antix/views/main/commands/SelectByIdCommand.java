package antix.views.main.commands;

import antix.model.MastodonPost;
import antix.utils.FeedbackUtils;
import antix.utils.GridUtils;
import antix.views.main.PostSelector;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;

import java.util.List;

/**
 * Commande permettant de sélectionner un post via son identifiant (ID exact).
 * Utile pour une recherche précise.
 */
public class SelectByIdCommand implements Command {
    private final Grid<MastodonPost> grid;
    private final PostSelector selector;
    private final Div contentDiv;

    /**
     * Constructeur de la commande SelectById.
     *
     * @param grid       Grille contenant les posts.
     * @param selector   Interface de sélection/affichage.
     * @param contentDiv Zone d'affichage pour les messages utilisateur.
     */
    public SelectByIdCommand(Grid<MastodonPost> grid, PostSelector selector, Div contentDiv) {
        this.grid = grid;
        this.selector = selector;
        this.contentDiv = contentDiv;
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
            FeedbackUtils.showError(contentDiv, "Veuillez spécifier un ID valide (ex: select 109384881).");
            return;
        }

        List<MastodonPost> items = GridUtils.fetchAll(grid);
        boolean found = items.stream()
                .filter(post -> String.valueOf(post.getId()).equals(id))
                .findFirst()
                .map(post -> {
                    selector.selectAndDisplay(post);
                    FeedbackUtils.showSuccess(contentDiv, "Post ID " + id + " sélectionné.");
                    return true;
                }).orElse(false);

        if (!found) {
            FeedbackUtils.showError(contentDiv, "Aucun post trouvé avec l'ID : " + id);
        }
    }

    @Override
    public String getDescription() {
        return "select <id> : sélectionne le post avec l'identifiant donné";
    }
}

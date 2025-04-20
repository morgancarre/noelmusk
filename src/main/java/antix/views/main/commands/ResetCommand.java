package antix.views.main.commands;

import antix.model.MastodonPost;
import antix.utils.FeedbackUtils;
import antix.views.main.PostSelector;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;

import java.util.List;
import java.util.function.Supplier;

/**
 * Commande permettant de réinitialiser l'affichage avec les derniers posts.
 * Elle recharge les données depuis un tag par défaut (ex. \"info\").
 */
public class ResetCommand implements Command {
    private final Grid<MastodonPost> grid;
    private final Supplier<List<MastodonPost>> fetcher;
    private final PostSelector selector;
    private final Div contentDiv;

    /**
     * Constructeur de la commande Reset.
     *
     * @param grid       Grille des posts.
     * @param fetcher    Fonction permettant de récupérer les posts (via API).
     * @param selector   Sélecteur/afficheur de post.
     * @param contentDiv Div contenant la grille.
     */
    public ResetCommand(Grid<MastodonPost> grid, Supplier<List<MastodonPost>> fetcher, PostSelector selector,
            Div contentDiv) {
        this.grid = grid;
        this.fetcher = fetcher;
        this.selector = selector;
        this.contentDiv = contentDiv;
    }

    /**
     * Réinitialise la grille avec les données récupérées.
     * Affiche automatiquement le premier post si possible.
     *
     * @param input Entrée utilisateur (ignorée ici).
     */
    @Override
    public void execute(String input) {
        List<MastodonPost> posts = fetcher.get();
        grid.setItems(posts);

        if (!posts.isEmpty()) {
            selector.selectAndDisplay(posts.get(0));
            FeedbackUtils.showSuccess(contentDiv, "Liste réinitialisée avec " + posts.size() + " posts.");
        } else {
            FeedbackUtils.showMessage(contentDiv, "Aucun post trouvé à réinitialiser.");
        }
    }

    /**
     * Description de la commande pour l'aide.
     *
     * @return Description textuelle.
     */
    @Override
    public String getDescription() {
        return "reset : réinitialise la liste avec les derniers posts du tag par défaut";
    }
}

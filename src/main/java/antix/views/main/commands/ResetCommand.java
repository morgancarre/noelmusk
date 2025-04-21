package antix.views.main.commands;

import antix.model.MastodonPost;
import antix.utils.FeedbackUtils;
import antix.views.main.PostSelector;

import com.vaadin.flow.component.grid.Grid;
import java.util.List;
import java.util.function.Supplier;

/**
 * Commande permettant de réinitialiser l'affichage avec les derniers posts.
 * Elle recharge les données depuis un tag par défaut (ex. \"info\").
 */
public class ResetCommand extends Command {
    private final Grid<MastodonPost> grid;
    private final Supplier<List<MastodonPost>> fetcher;
    private final PostSelector selector;

    /**
     * Constructeur de la commande Reset.
     *
     * @param grid       Grille des posts.
     * @param fetcher    Fonction permettant de récupérer les posts (via API).
     * @param selector   Sélecteur/afficheur de post.
     */
    public ResetCommand(Grid<MastodonPost> grid, Supplier<List<MastodonPost>> fetcher, PostSelector selector) {
        super(List.of("reset"), "Reset", "reset : réinitialise la liste avec les derniers posts du tag par défaut");
        this.grid = grid;
        this.fetcher = fetcher;
        this.selector = selector;
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
            FeedbackUtils.showSuccess("Liste réinitialisée avec " + posts.size() + " posts.");
        } else {
            FeedbackUtils.showMessage("Aucun post trouvé à réinitialiser.");
        }
    }
}

package antix.views.main.commands;

import antix.model.MastodonPost;
import antix.utils.FeedbackUtils;
import antix.utils.GridUtils;
import antix.views.main.PostSelector;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Commande filtrant les posts ayant plus de n réponses.
 * Exemple : \"replies > 10\" ou \"r > 5\"
 */
public class RepliesGreaterCommand implements Command {
    private final Grid<MastodonPost> grid;
    private final PostSelector selector;
    private final Div feedbackDiv;

    /**
     * Constructeur.
     *
     * @param grid        Grille contenant les posts.
     * @param selector    Interface de sélection du post à afficher.
     * @param feedbackDiv Div contenant le contenu.
     */
    public RepliesGreaterCommand(Grid<MastodonPost> grid, PostSelector selector, Div feedbackDiv) {
        this.grid = grid;
        this.selector = selector;
        this.feedbackDiv = feedbackDiv;
    }

    /**
     * Exécution de la commande : filtre les posts selon la syntaxe \"replies > n\".
     *
     * @param input Entrée utilisateur.
     */
    @Override
    public void execute(String input) {
        String[] parts = input.split(">");
        if (parts.length < 2) {
            FeedbackUtils.showError(feedbackDiv, "Format attendu : replies > n");
            return;
        }

        String valuePart = parts[1].trim();
        if (valuePart.isEmpty()) {
            FeedbackUtils.showError(feedbackDiv, "Veuillez spécifier une valeur numérique après >.");
            return;
        }

        int min;
        try {
            min = Integer.parseInt(valuePart);
        } catch (NumberFormatException e) {
            FeedbackUtils.showError(feedbackDiv, "Nombre invalide : '" + valuePart + "'");
            return;
        }

        List<MastodonPost> filtered = GridUtils.fetchAll(grid).stream()
                .filter(post -> post.getRepliesCount() > min)
                .collect(Collectors.toList());

        grid.setItems(filtered);
        if (!filtered.isEmpty()) {
            selector.selectAndDisplay(filtered.get(0));
            FeedbackUtils.showSuccess(feedbackDiv, filtered.size() + " post(s) trouvés avec > " + min + " réponses.");
        } else {
            FeedbackUtils.showMessage(feedbackDiv, "Aucun post trouvé avec plus de " + min + " réponses.");
        }
    }

    /**
     * Description utilisée dans l'aide.
     *
     * @return Description texte.
     */
    @Override
    public String getDescription() {
        return "r > <n> / replies > <n> : filtrer les posts avec plus de n réponses";
    }
}

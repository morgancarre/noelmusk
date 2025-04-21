package antix.views.main.commands;

import antix.model.MastodonPost;
import antix.utils.FeedbackUtils;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.textfield.TextField;

/**
 * Commande permettant de copier le lien d’un post sélectionné.
 * Le lien est automatiquement copié dans le presse-papiers.
 */
public class LinkCommand implements Command {
    private final Grid<MastodonPost> grid;
    private final Div contentDiv;

    /**
     * Constructeur.
     *
     * @param grid        Grille des posts.
     * @param contentDiv  Zone d'affichage secondaire (feedback lien).
     */
    public LinkCommand(Grid<MastodonPost> grid, Div contentDiv) {
        this.grid = grid;
        this.contentDiv = contentDiv;
    }

    /**
     * Génére un champ contenant l’URL du post, prêt à être copié.
     *
     * @param input Entrée utilisateur (non utilisée).
     */
    @Override
    public void execute(String input) {
        contentDiv.removeAll();
        MastodonPost post = grid.getSelectedItems().stream().findFirst().orElse(null);

        if (post != null) {
            String url = post.getUrl();

            TextField linkField = new TextField("Lien du post");
            linkField.setValue(url);
            linkField.setReadOnly(true);
            linkField.setWidthFull();
            linkField.focus();

            linkField.getElement().executeJs("navigator.clipboard.writeText($0)", url);

            contentDiv.add(linkField);
            FeedbackUtils.showSuccess("Lien copié dans le presse-papiers !");
        } else {
            FeedbackUtils.showError("Aucun post sélectionné pour générer un lien.");
        }
    }

    /**
     * Retourne une description de la commande.
     *
     * @return Texte descriptif pour la commande d'aide.
     */
    @Override
    public String getDescription() {
        return "l / link : copier le lien du post sélectionné dans le presse-papier";
    }
}

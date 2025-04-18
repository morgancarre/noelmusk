package antix.views.main.commands;

import antix.model.MastodonPost;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.textfield.TextField;

public class LinkCommand implements Command {
    private final Grid<MastodonPost> grid;
    private final Div contentDiv;

    public LinkCommand(Grid<MastodonPost> grid, Div contentDiv) {
        this.grid = grid;
        this.contentDiv = contentDiv;
    }

    @Override
    public void execute(String input) {
        contentDiv.removeAll();
        MastodonPost post = grid.getSelectedItems().stream().findFirst().orElse(null);
        if (post != null) {
            String url = "https://mastodon.social/@" + post.getAccount().getUsername() + "/" + post.getId();

            TextField linkField = new TextField("Lien du post");
            linkField.setValue(url);
            linkField.setReadOnly(true);
            linkField.setWidthFull();
            linkField.focus(); // focus clavier direct

            contentDiv.add(linkField);
        } else {
            contentDiv.add(new Div("Aucun post sélectionné."));
        }
    }

    @Override
    public String getDescription() {
        return "l / link : afficher le lien du post sélectionné dans un champ copiable";
    }
}

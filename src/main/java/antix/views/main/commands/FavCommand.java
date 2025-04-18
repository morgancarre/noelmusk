package antix.views.main.commands;

import antix.model.MastodonPost;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import java.util.List;

public class FavCommand implements Command {
    private final Grid<MastodonPost> grid;
    private final List<MastodonPost> favoris;
    private final Div contentDiv;

    public FavCommand(Grid<MastodonPost> grid, List<MastodonPost> favoris, Div contentDiv) {
        this.grid = grid;
        this.favoris = favoris;
        this.contentDiv = contentDiv;
    }

    @Override
    public String getDescription() {
        return "f / fav : ajouter le post sélectionné aux favoris";
    }

    @Override
    public void execute(String input) {
        MastodonPost post = grid.getSelectedItems().stream().findFirst().orElse(null);
        if (post != null && !favoris.contains(post)) {
            favoris.add(post);
            contentDiv.removeAll();
            contentDiv.add(new Div("Ajouté aux favoris !"));
        }
    }
}

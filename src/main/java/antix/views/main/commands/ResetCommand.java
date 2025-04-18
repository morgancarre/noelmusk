package antix.views.main.commands;

import antix.model.MastodonPost;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;

import java.util.List;
import java.util.function.Supplier;

public class ResetCommand implements Command {
    private final Grid<MastodonPost> grid;
    private final Div contentDiv;
    private final Supplier<List<MastodonPost>> initialFetcher;

    public ResetCommand(Grid<MastodonPost> grid, Div contentDiv, Supplier<List<MastodonPost>> initialFetcher) {
        this.grid = grid;
        this.contentDiv = contentDiv;
        this.initialFetcher = initialFetcher;
    }

    @Override
    public void execute(String input) {
        List<MastodonPost> posts = initialFetcher.get();
        grid.setItems(posts);
        if (!posts.isEmpty())
            grid.select(posts.get(0));
        contentDiv.removeAll();
        contentDiv.add(new Div("Grille réinitialisée."));
    }

    @Override
    public String getDescription() {
        return "reset : réinitialise la grille à l'état initial";
    }
}

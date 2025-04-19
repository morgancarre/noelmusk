package antix.views.main.commands;

import antix.model.MastodonPost;
import com.vaadin.flow.component.grid.Grid;

import java.util.List;
import java.util.function.Supplier;

public class ResetCommand implements Command {
    private final Grid<MastodonPost> grid;
    private final Supplier<List<MastodonPost>> initialFetcher;

    public ResetCommand(Grid<MastodonPost> grid, Supplier<List<MastodonPost>> initialFetcher) {
        this.grid = grid;
        this.initialFetcher = initialFetcher;
    }

    @Override
    public void execute(String input) {
        List<MastodonPost> posts = initialFetcher.get();
        grid.setItems(posts);
        if (!posts.isEmpty())
            grid.select(posts.get(0));
    }

    @Override
    public String getDescription() {
        return "reset : réinitialise la grille à l'état initial";
    }
}

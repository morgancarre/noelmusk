package antix.views.main.commands;

import antix.model.MastodonPost;
import com.vaadin.flow.component.grid.Grid;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class RandCommand implements Command {
    private final Grid<MastodonPost> grid;

    public RandCommand(Grid<MastodonPost> grid) {
        this.grid = grid;
    }

    @Override
    public void execute(String input) {
        List<MastodonPost> items = grid.getDataProvider().fetch(new com.vaadin.flow.data.provider.Query<>())
                .collect(Collectors.toList());
        if (!items.isEmpty()) {
            MastodonPost random = items.get(new Random().nextInt(items.size()));
            grid.select(random);
        }
    }

    @Override
    public String getDescription() {
        return "rand / random : sélectionne un post aléatoire dans la grille actuelle";
    }
}

package antix.views.main.commands;

import antix.model.MastodonPost;
import com.vaadin.flow.component.grid.Grid;

import java.util.List;
import java.util.stream.Collectors;

public class SelectByIdCommand implements Command {
    private final Grid<MastodonPost> grid;

    public SelectByIdCommand(Grid<MastodonPost> grid) {
        this.grid = grid;
    }

    @Override
    public void execute(String input) {
        try {
            String id = input.replaceFirst("select\\s+", "").trim();
            List<MastodonPost> items = grid.getDataProvider().fetch(new com.vaadin.flow.data.provider.Query<>())
                    .collect(Collectors.toList());
            items.stream().filter(p -> String.valueOf(p.getId()).equals(id)).findFirst().ifPresent(grid::select);
        } catch (NumberFormatException ignored) {
        }
    }

    @Override
    public String getDescription() {
        return "select <id> : sélectionne le post avec l'identifiant donné";
    }
}

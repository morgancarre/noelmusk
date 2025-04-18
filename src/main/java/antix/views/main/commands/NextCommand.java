package antix.views.main.commands;

import antix.model.MastodonPost;
import com.vaadin.flow.component.grid.Grid;

import java.util.List;
import java.util.stream.Collectors;

public class NextCommand implements Command {
    private final Grid<MastodonPost> grid;

    public NextCommand(Grid<MastodonPost> grid) {
        this.grid = grid;
    }

    @Override
    public void execute(String input) {
        List<MastodonPost> items = grid.getDataProvider().fetch(new com.vaadin.flow.data.provider.Query<>())
                .collect(Collectors.toList());
        MastodonPost current = grid.getSelectedItems().stream().findFirst().orElse(null);
        if (current != null) {
            int index = items.indexOf(current);
            if (index >= 0 && index < items.size() - 1) {
                grid.select(items.get(index + 1));
            }
        } else if (!items.isEmpty()) {
            grid.select(items.get(0));
        }
    }

    @Override
    public String getDescription() {
        return "n / next : sélectionner le post suivant";
    }
}

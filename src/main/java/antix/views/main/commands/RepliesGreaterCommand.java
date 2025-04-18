package antix.views.main.commands;

import antix.model.MastodonPost;
import com.vaadin.flow.component.grid.Grid;

import java.util.List;
import java.util.stream.Collectors;

public class RepliesGreaterCommand implements Command {
    private final Grid<MastodonPost> grid;

    public RepliesGreaterCommand(Grid<MastodonPost> grid) {
        this.grid = grid;
    }

    @Override
    public void execute(String input) {
        try {
            int min = Integer.parseInt(input.substring(input.indexOf(">") + 1).trim());
            List<MastodonPost> all = grid.getDataProvider().fetch(new com.vaadin.flow.data.provider.Query<>())
                    .collect(Collectors.toList());
            List<MastodonPost> filtered = all.stream()
                    .filter(p -> p.getRepliesCount() > min)
                    .collect(Collectors.toList());
            grid.setItems(filtered);
            if (!filtered.isEmpty())
                grid.select(filtered.get(0));
        } catch (Exception ignored) {
        }
    }

    @Override
    public String getDescription() {
        return "r > <n> / replies > <n> : filtrer les posts avec plus de n réponses";
    }
}

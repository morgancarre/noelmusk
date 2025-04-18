package antix.views.main.commands;

import antix.model.MastodonPost;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.data.provider.Query;

import java.util.List;
import java.util.stream.Collectors;

public class GotoCommand implements Command {
    private final Grid<MastodonPost> grid;

    public GotoCommand(Grid<MastodonPost> grid) {
        this.grid = grid;
    }

    @Override
    public void execute(String input) {
        try {
            int index = Integer.parseInt(input.replaceFirst("goto\\s+|g\\s+", "").trim()) - 1;
            List<MastodonPost> posts = grid.getDataProvider().fetch(new Query<>())
                    .collect(Collectors.toList());
            if (index >= 0 && index < posts.size()) {
                grid.select(posts.get(index));
            }
        } catch (Exception ignored) {
        }
    }

    @Override
    public String getDescription() {
        return "g <n> / goto <n> : aller au post numéro n";
    }
}

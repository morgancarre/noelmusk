package antix.views.main.commands;

import antix.model.MastodonPost;
import com.vaadin.flow.component.grid.Grid;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class TopCommand implements Command {
    private final Grid<MastodonPost> grid;

    public TopCommand(Grid<MastodonPost> grid) {
        this.grid = grid;
    }

    @Override
    public void execute(String input) {
        List<MastodonPost> items = grid.getDataProvider().fetch(new com.vaadin.flow.data.provider.Query<>())
                .collect(Collectors.toList());
        Optional<MastodonPost> top = items.stream().max(Comparator.comparingInt(MastodonPost::getRepliesCount));
        top.ifPresent(grid::select);
    }

    @Override
    public String getDescription() {
        return "top : sélectionne le post avec le plus de réponses";
    }
}

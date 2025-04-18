package antix.views.main.commands;

import antix.model.MastodonPost;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.data.provider.Query;

import java.util.List;
import java.util.stream.Collectors;

public class SortRepliesCommand implements Command {
    private final Grid<MastodonPost> grid;

    public SortRepliesCommand(Grid<MastodonPost> grid) {
        this.grid = grid;
    }

    @Override
    public String getDescription() {
        return "sort replies : trie les posts par nombre de réponses décroissant";
    }

    @Override
    public void execute(String input) {
        List<MastodonPost> items = grid.getDataProvider().fetch(new Query<>()).collect(Collectors.toList());
        items.sort((a, b) -> Integer.compare(b.getRepliesCount(), a.getRepliesCount()));
        grid.setItems(items);
        if (!items.isEmpty()) {
            grid.select(items.get(0));
        }
    }

}

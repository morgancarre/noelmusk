package antix.views.main.commands;

import antix.model.MastodonPost;
import antix.utils.GridUtils;
import antix.views.main.PostSelector;

import com.vaadin.flow.component.grid.Grid;

import java.util.List;

public abstract class NavigationCommand implements Command {
    protected final Grid<MastodonPost> grid;
    protected final PostSelector selector;

    public NavigationCommand(Grid<MastodonPost> grid, PostSelector selector) {
        this.grid = grid;
        this.selector = selector;
    }

    protected abstract MastodonPost getTargetPost(List<MastodonPost> items, MastodonPost current);

    @Override
    public void execute(String input) {
        List<MastodonPost> items = GridUtils.fetchAll(grid);
        if (items.isEmpty())
            return;

        MastodonPost current = grid.asSingleSelect().getValue();
        MastodonPost target = getTargetPost(items, current);

        if (target != null) {
            selector.selectAndDisplay(target);
        }
    }
}

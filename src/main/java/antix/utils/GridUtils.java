package antix.utils;

import antix.model.MastodonPost;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.data.provider.Query;

import java.util.List;
import java.util.stream.Collectors;

public final class GridUtils {
    private GridUtils() {
    }

    public static List<MastodonPost> fetchAll(Grid<MastodonPost> grid) {
        return grid.getDataProvider()
                .fetch(new Query<>())
                .collect(Collectors.toList());
    }
}

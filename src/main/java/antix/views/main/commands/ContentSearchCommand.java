package antix.views.main.commands;

import antix.model.MastodonPost;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import org.jsoup.Jsoup;

import java.util.List;
import java.util.stream.Collectors;

public class ContentSearchCommand implements Command {
    private final Grid<MastodonPost> grid;
    private final Div contentDiv;

    public ContentSearchCommand(Grid<MastodonPost> grid, Div contentDiv) {
        this.grid = grid;
        this.contentDiv = contentDiv;
    }

    @Override
    public void execute(String input) {
        String query = input.replaceFirst("c\\s*\"?", "").replaceAll("\"$", "").trim().toLowerCase();
        List<MastodonPost> items = grid.getDataProvider().fetch(new com.vaadin.flow.data.provider.Query<>())
                .collect(Collectors.toList());

        List<MastodonPost> filtered = items.stream()
                .filter(p -> Jsoup.parse(p.getContent()).text().toLowerCase().contains(query))
                .collect(Collectors.toList());

        grid.setItems(filtered);
        if (!filtered.isEmpty())
            grid.select(filtered.get(0));
        contentDiv.removeAll();
        contentDiv.add(new Div("Résultats pour : \"" + query + "\""));
    }

    @Override
    public String getDescription() {
        return "c \"mot\" : recherche dans le contenu des posts";
    }
}

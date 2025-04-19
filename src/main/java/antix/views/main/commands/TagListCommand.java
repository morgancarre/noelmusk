package antix.views.main.commands;

import antix.model.MastodonPost;
import antix.model.Tag;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;

import java.util.*;
import java.util.stream.Collectors;

public class TagListCommand implements Command {
    private final Grid<MastodonPost> grid;
    private final Div contentDiv;

    public TagListCommand(Grid<MastodonPost> grid, Div contentDiv) {
        this.grid = grid;
        this.contentDiv = contentDiv;
    }

    @Override
    public void execute(String input) {
        List<MastodonPost> items = grid.getDataProvider().fetch(new com.vaadin.flow.data.provider.Query<>())
                .collect(Collectors.toList());
        Map<String, Integer> tagFrequency = new HashMap<>();

        for (MastodonPost post : items) {
            if (post.getTags() != null) {
                for (Tag tag : post.getTags()) {
                    tagFrequency.put(tag.getName(), tagFrequency.getOrDefault(tag.getName(), 0) + 1);
                }
            }
        }

        StringBuilder out = new StringBuilder("Tags les plus fréquents :\n");
        tagFrequency.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .limit(20)
                .forEach(entry -> out.append(entry.getKey()).append(" (").append(entry.getValue()).append(")\n"));

        contentDiv.removeAll();
        Div div = new Div();
        div.getStyle().set("white-space", "pre-wrap");
        div.setText(out.toString());
        contentDiv.add(div);
    }

    @Override
    public String getDescription() {
        return "taglist : liste les tags les plus fréquents dans les posts affichés";
    }
}

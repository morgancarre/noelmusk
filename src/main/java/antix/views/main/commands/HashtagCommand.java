package antix.views.main.commands;

import antix.model.MastodonPost;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class HashtagCommand implements Command {
    private final Grid<MastodonPost> grid;
    private final Div contentDiv;
    private final Function<String, List<MastodonPost>> tagFetcher;

    public HashtagCommand(Grid<MastodonPost> grid, Div contentDiv,
            Function<String, List<MastodonPost>> tagFetcher) {
        this.grid = grid;
        this.contentDiv = contentDiv;
        this.tagFetcher = tagFetcher;
    }

    @Override
    public void execute(String input) {
        contentDiv.removeAll();

        if (!input.contains(" ")) {
            contentDiv.setText("❌ Veuillez spécifier un ou plusieurs hashtags après la commande.");
            return;
        }

        String query = input.trim().substring(input.indexOf(" ") + 1).trim();
        if (StringUtils.isBlank(query)) {
            contentDiv.setText("❌ La requête est vide.");
            return;
        }

        Set<String> andTags = new HashSet<>();
        Set<String> orTags = new HashSet<>();
        Set<String> notTags = new HashSet<>();
        List<String> filters = new ArrayList<>();

        String[] parts = query.split("\\s+|(?<=&&)|(?=&&)|(?<=\\|\\|)|(?=\\|\\|)|(?<=\\|)|(?=\\|)");
        String mode = "AND";

        for (String part : parts) {
            String p = part.trim();
            if (p.isEmpty())
                continue;

            if (p.startsWith("likes:") || p.startsWith("reposts:")) {
                filters.add(p);
                continue;
            }

            switch (p) {
                case "&&", "et" -> mode = "AND";
                case "||", "|", "ou", "v" -> mode = "OR";
                default -> {
                    if (p.startsWith("!")) {
                        notTags.add(p.substring(1).toLowerCase());
                    } else {
                        if ("AND".equals(mode)) {
                            andTags.add(p.toLowerCase());
                        } else {
                            orTags.add(p.toLowerCase());
                        }
                    }
                }
            }
        }

        Set<MastodonPost> result = new HashSet<>();

        if (!andTags.isEmpty()) {
            List<MastodonPost> baseList = tagFetcher.apply(andTags.iterator().next());
            result = baseList.stream()
                    .filter(post -> {
                        Set<String> lowerTags = post.getTags().stream()
                                .map(t -> t.getName().toLowerCase())
                                .collect(Collectors.toSet());
                        return andTags.stream().allMatch(lowerTags::contains);
                    })
                    .collect(Collectors.toSet());
        }

        if (!orTags.isEmpty()) {
            Set<MastodonPost> orResults = new HashSet<>();
            for (String tag : orTags) {
                orResults.addAll(tagFetcher.apply(tag));
            }
            if (result.isEmpty()) {
                result = orResults;
            } else {
                result.addAll(orResults); // combine les deux
            }
        }

        if (andTags.isEmpty() && orTags.isEmpty()) {
            result = new HashSet<>(tagFetcher.apply(query));
        }

        if (!notTags.isEmpty()) {
            result = result.stream()
                    .filter(post -> post.getTags().stream()
                            .noneMatch(t -> notTags.contains(t.getName().toLowerCase())))
                    .collect(Collectors.toSet());
        }

        for (String filter : filters) {
            if (filter.startsWith("likes:")) {
                result = applyNumericFilter(result, filter.substring(6), MastodonPost::getFavouritesCount);
            } else if (filter.startsWith("reposts:")) {
                result = applyNumericFilter(result, filter.substring(8), MastodonPost::getReblogsCount);
            }
        }

        List<MastodonPost> finalList = result.stream()
            .limit(15)
            .collect(Collectors.toList());

        finalList.sort(Comparator.comparing(MastodonPost::getId).reversed());

        if (finalList.isEmpty()) {
            contentDiv.setText("⚠️ Aucun post trouvé pour cette requête : \"" + query + "\"");
        } else {
            grid.setItems(finalList);
            grid.select(finalList.get(0));
        }
    }

    @Override
    public String getDescription() {
        return "h <tag(s)> : recherche avancée avec opérateurs :\n"
                + "  && (et), || (ou), ! (exclure),\n"
                + "  likes:>x, reposts:<y, etc.\n"
                + "Ex: h squeezie && react || video !politique likes:>5";
    }

    private Set<MastodonPost> applyNumericFilter(Set<MastodonPost> posts, String condition,
            Function<MastodonPost, Integer> extractor) {
        char operator = condition.charAt(0);
        int value;
        try {
            value = Integer.parseInt(condition.substring(1));
        } catch (NumberFormatException e) {
            return posts;
        }

        return switch (operator) {
            case '>' -> posts.stream().filter(p -> extractor.apply(p) > value).collect(Collectors.toSet());
            case '<' -> posts.stream().filter(p -> extractor.apply(p) < value).collect(Collectors.toSet());
            case '=' -> posts.stream().filter(p -> extractor.apply(p) == value).collect(Collectors.toSet());
            default -> posts;
        };
    }

}

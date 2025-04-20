package antix.views.main.commands;

import antix.model.MastodonPost;
import antix.utils.FeedbackUtils;
import antix.views.main.PostSelector;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Commande de recherche avancée via hashtags, avec opérateurs logiques
 * (AND, OR, NOT) et filtres numériques (likes, reposts).
 * <p>
 * Exemples :
 * - h sport && musique
 * - h actus !politique likes:>5 reposts:<3
 * - h dev || ai && code !blabla
 */
public class HashtagCommand implements Command {
    private final Grid<MastodonPost> grid;
    private final Div feedbackDiv;
    private final Function<String, List<MastodonPost>> tagFetcher;
    private final PostSelector selector;

    /**
     * Constructeur.
     *
     * @param grid        Grille contenant les posts.
     * @param feedbackDiv Zone d'affichage pour les messages utilisateur.
     * @param tagFetcher  Fonction de récupération des posts par tag.
     * @param selector    Sélecteur pour afficher un post.
     */
    public HashtagCommand(Grid<MastodonPost> grid, Div feedbackDiv,
            Function<String, List<MastodonPost>> tagFetcher,
            PostSelector selector) {
        this.grid = grid;
        this.feedbackDiv = feedbackDiv;
        this.tagFetcher = tagFetcher;
        this.selector = selector;
    }

    /**
     * Exécute la commande de recherche avec opérateurs logiques et filtres.
     *
     * @param input Entrée utilisateur après le mot-clé 'h' ou 'hashtag'.
     */
    @Override
    public void execute(String input) {
        feedbackDiv.removeAll();

        if (!input.contains(" ")) {
            FeedbackUtils.showError(feedbackDiv, "Veuillez spécifier un ou plusieurs hashtags après la commande.");
            return;
        }

        String query = input.trim().substring(input.indexOf(" ") + 1).trim();
        if (StringUtils.isBlank(query)) {
            FeedbackUtils.showError(feedbackDiv, "La requête est vide.");
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
                    if (p.startsWith("!"))
                        notTags.add(p.substring(1).toLowerCase());
                    else if ("AND".equals(mode))
                        andTags.add(p.toLowerCase());
                    else
                        orTags.add(p.toLowerCase());
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
            if (result.isEmpty())
                result = orResults;
            else
                result.addAll(orResults);
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
                .limit(40)
                .sorted(Comparator.comparing(MastodonPost::getId).reversed())
                .collect(Collectors.toList());

        if (finalList.isEmpty()) {
            FeedbackUtils.showMessage(feedbackDiv, "Aucun post trouvé pour : \"" + query + "\"");
        } else {
            grid.setItems(finalList);
            selector.selectAndDisplay(finalList.get(0));
            FeedbackUtils.showSuccess(feedbackDiv, finalList.size() + " post(s) trouvés pour : " + query);
        }
    }

    /**
     * Applique un filtre numérique sur un ensemble de posts.
     *
     * @param posts     Ensemble de posts à filtrer.
     * @param condition Condition au format texte, ex: >5
     * @param extractor Fonction d’extraction du champ numérique.
     * @return Sous-ensemble filtré.
     */
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

    /**
     * Description utilisée dans la commande `help`.
     *
     * @return Texte d’aide affiché à l’utilisateur.
     */
    @Override
    public String getDescription() {
        return "h <tag(s)> : recherche avancée avec opérateurs :\n"
                + "  && (et), || (ou), ! (exclure),\n"
                + "  likes:>x, reposts:<y, etc.\n"
                + "Ex: h squeezie && react || video !politique likes:>5";
    }
}

package antix.factory;

import antix.model.MastodonPost;
import antix.views.main.PostSelector;
import antix.views.main.commands.*;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;

import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Fabrique centralisée pour construire toutes les commandes disponibles.
 * Permet de regrouper leur instanciation et de les passer à l'application.
 */
public class CommandFactory {

    /**
     * Construit l’ensemble des commandes disponibles.
     *
     * @param grid         Grille d'affichage des posts.
     * @param contentDiv   Div utilisé pour afficher du contenu (HTML,
     *                     formulaire...).
     * @param selector     Sélecteur de post à afficher.
     * @param favoris      Liste des favoris à mettre à jour.
     * @param resetFetcher Fonction de récupération initiale (ex: tag par défaut).
     * @param tagFetcher   Fonction de récupération des posts par hashtag.
     * @return Map des commandes associées à leurs mots-clés.
     */
    public static Map<String, Command> build(
            Grid<MastodonPost> grid,
            Div contentDiv,
            PostSelector selector,
            List<MastodonPost> favoris,
            Supplier<List<MastodonPost>> resetFetcher,
            Function<String, List<MastodonPost>> tagFetcher) {
        Map<String, Command> commands = new LinkedHashMap<>();

        commands.put("reset", new ResetCommand(grid, resetFetcher, selector));
        commands.put("replies", new RepliesGreaterCommand(grid, selector));
        commands.put("r", commands.get("replies")); // alias
        commands.put("select", new SelectByIdCommand(grid, selector));
        commands.put("c", new ContentSearchCommand(grid, selector));
        commands.put("h", new HashtagCommand(grid, tagFetcher, selector));
        commands.put("hashtag", commands.get("h")); // alias
        commands.put("l", new LinkCommand(grid, contentDiv));
        commands.put("link", commands.get("l")); // alias
        commands.put("f", new FavCommand(grid, favoris));
        commands.put("fav", commands.get("f")); // alias

        // Commande d'aide
        HelpCommand helpCmd = new HelpCommand(commands, contentDiv);
        commands.put("help", helpCmd);
        commands.put("?", helpCmd); // alias

        // Navigation simple (pas besoin de message)
        Command next = new NextCommand(grid, selector);
        Command prev = new PreviousCommand(grid, selector);
        Command top = new TopCommand(grid, selector);
        Command bottom = new BottomCommand(grid, selector);
        Command rand = new RandCommand(grid, selector);
        Command play = new PlayCommand(grid);
        Command stop = new StopCommand((PlayCommand) play);

        commands.put("next", next);
        commands.put("n", next);
        commands.put("previous", prev);
        commands.put("p", prev);
        commands.put("top", top);
        commands.put("bottom", bottom);
        commands.put("rand", rand);
        commands.put("random", rand);
        commands.put("play", play);
        commands.put("stop", stop);

        // Commandes spécifiques
        commands.put("g", new GotoCommand(grid, selector));
        commands.put("goto", commands.get("g"));

        commands.put("sort replies", new SortRepliesCommand(grid, selector));
        commands.put("taglist", new TagListCommand(grid, contentDiv));

        return commands;
    }
}

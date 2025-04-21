package antix.views.main.commands;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;

import antix.components.CommandCard;

import java.util.*;

/**
 * Commande affichant l'aide utilisateur avec la liste des commandes
 * disponibles.
 */
public class HelpCommand extends Command {
    private final Map<String, Command> commandMap;
    private final Div contentDiv;

    /**
     * Constructeur.
     *
     * @param commandMap Ensemble des commandes enregistrées.
     * @param contentDiv Zone dans laquelle afficher l'aide.
     */
    public HelpCommand(Map<String, Command> commandMap, Div contentDiv) {
        super("Help", "? / help : affiche cette aide");
        this.commandMap = commandMap;
        this.contentDiv = contentDiv;
    }

    /**
     * Affiche les descriptions des commandes sans doublon.
     *
     * @param input Entrée utilisateur (non utilisée ici).
     */
    @Override
    public void execute(String input) {
        contentDiv.removeAll();

        Div helpDiv = new Div();
        helpDiv.add(new H2("Commandes disponibles :"));
        helpDiv.getStyle().set("white-space", "pre-wrap");

        Div commandsDiv = new Div();
        commandsDiv.getStyle().set("display", "flex");
        commandsDiv.getStyle().set("flex-wrap", "wrap");
        helpDiv.add(commandsDiv);


        Set<String> seenDescriptions = new HashSet<>();
        Set<Command> uniqueCommands = new LinkedHashSet<>(commandMap.values());

        uniqueCommands.forEach(cmd -> {
            String desc = cmd.getDescription();
            if (seenDescriptions.add(desc)) {
                commandsDiv.add(new CommandCard(cmd));
                // helpDiv.add(new Paragraph(desc));
            }
        });

        contentDiv.add(helpDiv);
    }
}

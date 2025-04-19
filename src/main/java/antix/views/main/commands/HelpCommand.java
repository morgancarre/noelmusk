package antix.views.main.commands;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;

import java.util.*;

public class HelpCommand implements Command {
    private final Map<String, Command> commandMap;
    private final Div contentDiv;

    public HelpCommand(Map<String, Command> commandMap, Div contentDiv) {
        this.commandMap = commandMap;
        this.contentDiv = contentDiv;
    }

    @Override
    public String getDescription() {
        return "? / help : affiche cette aide";
    }

    @Override
    public void execute(String input) {
        contentDiv.removeAll();
        Div helpDiv = new Div();
        helpDiv.getStyle().set("white-space", "pre-wrap");

        helpDiv.add(new H2("Commandes disponibles :"));

        Set<String> seenDescriptions = new HashSet<>();
        Set<Command> uniqueCommands = new LinkedHashSet<>(commandMap.values());

        uniqueCommands.forEach(cmd -> {
            String desc = cmd.getDescription();
            if (seenDescriptions.add(desc)) {
                helpDiv.add(new Paragraph(desc));
            }
        });

        contentDiv.add(helpDiv);
    }
}

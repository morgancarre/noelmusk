package antix.views.main.commands;

import com.vaadin.flow.component.html.Div;

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

        StringBuilder helpText = new StringBuilder("Commandes disponibles :\n");

        Set<String> seenDescriptions = new HashSet<>();
        new HashSet<>(commandMap.values()).forEach(cmd -> {
            String desc = cmd.getDescription();
            if (seenDescriptions.add(desc)) {
                helpText.append(desc).append("\n");
            }
        });

        helpDiv.setText(helpText.toString().trim());
        contentDiv.add(helpDiv);
    }
}

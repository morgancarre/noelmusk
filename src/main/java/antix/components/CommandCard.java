package antix.components;

import com.vaadin.flow.component.html.Div;

import antix.views.main.commands.Command;

public class CommandCard extends Div {

    public CommandCard(Command command) {
        addClassName("command-card");
        setWidth("30%");
        setText(command.getTitle());
    }

}

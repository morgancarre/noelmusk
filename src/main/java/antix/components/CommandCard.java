package antix.components;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Span;

import antix.views.main.commands.Command;

public class CommandCard extends Div {

    public CommandCard(Command command) {
        addClassName("command-card");
        setWidth("30%");
        getStyle().set("margin", "5px");
        H3 title = new H3(command.getTitle());
        Span aliases = new Span("[" + String.join(" / ", command.getAliases()) + "]");

        add(title, aliases);
    }
}

package antix.views.main.commands;

@FunctionalInterface
public interface Command {
    void execute(String input);

    default String getDescription() {
        return "Aucune description disponible.\n";
    }
}

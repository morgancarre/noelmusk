package antix.views.main.commands;

public abstract class Command {
    private final String title;
    private final String description;

    // Constructeur pour forcer les sous-classes à fournir un titre et une description
    protected Command(String title, String description) {
        this.title = title;
        this.description = description;
    }

    // Méthode abstraite pour exécuter la commande
    public abstract void execute(String input);

    // Getter pour le titre
    public String getTitle() {
        return title;
    }

    // Getter pour la description
    public String getDescription() {
        return description;
    }
}
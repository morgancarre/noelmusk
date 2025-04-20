package antix.views.main.commands;

/**
 * Commande qui arrête la lecture automatique lancée par la commande Play.
 */
public class StopCommand implements Command {
    private final PlayCommand playCommand;

    /**
     * Constructeur.
     *
     * @param playCommand Référence à la commande Play à contrôler.
     */
    public StopCommand(PlayCommand playCommand) {
        this.playCommand = playCommand;
    }

    /**
     * Arrête la lecture automatique.
     *
     * @param input Entrée utilisateur (ignorée ici).
     */
    @Override
    public void execute(String input) {
        playCommand.stop();
    }

    @Override
    public String getDescription() {
        return "stop : arrête la lecture automatique";
    }
}

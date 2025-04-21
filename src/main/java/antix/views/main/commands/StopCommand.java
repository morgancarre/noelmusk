package antix.views.main.commands;

/**
 * Commande qui arrête la lecture automatique lancée par la commande Play.
 */
public class StopCommand extends Command {
    private final PlayCommand playCommand;

    /**
     * Constructeur.
     *
     * @param playCommand Référence à la commande Play à contrôler.
     */
    public StopCommand(PlayCommand playCommand) {
        super("Stop", "stop : arrête la lecture automatique");
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
}

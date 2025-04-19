package antix.views.main.commands;

public class StopCommand implements Command {
    private final PlayCommand playCommand;

    public StopCommand(PlayCommand playCommand) {
        this.playCommand = playCommand;
    }

    @Override
    public void execute(String input) {
        playCommand.stop();
    }

    @Override
    public String getDescription() {
        return "stop : arrête la lecture automatique";
    }
}

package antix.views.main.commands;

import antix.model.MastodonPost;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.grid.Grid;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class PlayCommand implements Command {
    private static Timer timer;
    private final Grid<MastodonPost> grid;

    public PlayCommand(Grid<MastodonPost> grid) {
        this.grid = grid;
    }

    @Override
    public void execute(String input) {
        stop(); // Arrête tout ancien timer

        int seconds = 3;
        try {
            seconds = Integer.parseInt(input.replaceFirst("play\\s*", "").trim());
        } catch (NumberFormatException ignored) {
        }

        List<MastodonPost> items = grid.getDataProvider().fetch(new com.vaadin.flow.data.provider.Query<>())
                .collect(Collectors.toList());

        if (items.isEmpty())
            return;

        // Trouver l’index de départ
        MastodonPost current = grid.asSingleSelect().getValue();
        int startIndex = (current != null) ? items.indexOf(current) : 0;
        AtomicInteger index = new AtomicInteger(startIndex);

        // ⚠️ Capturer l'UI maintenant, pendant qu’on est dans le thread principal
        UI ui = UI.getCurrent();

        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (ui == null)
                    return;

                ui.access(() -> {
                    if (items.isEmpty())
                        return;

                    int i = index.getAndUpdate(val -> (val + 1) % items.size());
                    MastodonPost post = items.get(i);
                    grid.select(post);
                });
            }
        }, 0, seconds * 1000L);
    }

    @Override
    public String getDescription() {
        return "play <n> : change de post automatiquement toutes les n secondes (rotation circulaire)";
    }

    public void stop() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }
}

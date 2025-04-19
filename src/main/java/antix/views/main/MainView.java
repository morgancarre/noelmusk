package antix.views.main;

import antix.model.MastodonPost;
import antix.views.main.commands.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.Query;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.utils.URIBuilder;
import org.jsoup.Jsoup;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Function;

@PageTitle("NeoMusk - Test pour la bande")
@Route("")
public class MainView extends VerticalLayout {
    public MainView() {
        // Cacher curseur et désactiver le clic
        var prompt = new TextField();
        prompt.setId("prompt-field");
        prompt.setWidth("100%");

        var promptContainer = new VerticalLayout(prompt);
        promptContainer.setWidth("100%");
        add(promptContainer);

        UI.getCurrent().getPage().executeJs("""
                    const overlay = document.createElement('div');
                    overlay.id = 'click-blocker';
                    overlay.style.position = 'fixed';
                    overlay.style.top = 0;
                    overlay.style.left = 0;
                    overlay.style.width = '100vw';
                    overlay.style.height = '100vh';
                    overlay.style.zIndex = '9999';
                    overlay.style.pointerEvents = 'all';
                    overlay.style.backgroundColor = 'transparent';
                    document.body.appendChild(overlay);

                    setTimeout(() => {
                        const promptField = document.querySelector('#prompt-field');
                        if (promptField && promptField.offsetParent !== null) {
                            const rect = promptField.getBoundingClientRect();
                            const hole = document.createElement('div');
                            hole.style.position = 'fixed';
                            hole.style.left = rect.left + 'px';
                            hole.style.top = rect.top + 'px';
                            hole.style.width = rect.width + 'px';
                            hole.style.height = rect.height + 'px';
                            hole.style.pointerEvents = 'none';
                            hole.style.zIndex = '10000';  // Au-dessus de l’overlay
                            document.body.appendChild(hole);

                            // S'assurer que l'élément réel est cliquable
                            promptField.style.zIndex = '10001';
                            promptField.style.position = 'relative';
                            promptField.style.pointerEvents = 'auto';
                        }
                    }, 100);
                """);

        setSizeFull();
        setAlignItems(FlexComponent.Alignment.CENTER);
        var grid = new Grid<>(MastodonPost.class, false);
        grid.setSelectionMode(Grid.SelectionMode.SINGLE);
        var contentDiv = new Div();
        contentDiv.setWidthFull();

        addIndexColumn(grid);
        addContentColumn(grid);
        grid.setItemDetailsRenderer(new ComponentRenderer<>(post -> new Div(Jsoup.parse(post.getContent()).text())));
        grid.setDetailsVisibleOnClick(false);

        AtomicBoolean internalChange = new AtomicBoolean(false);
        // Liste des favoris en mémoire
        List<MastodonPost> favoris = new ArrayList<>();

        // Commandes
        Map<String, Command> commandMap = new HashMap<>();
        Function<String, List<MastodonPost>> tagFetcher = this::fetchPostsFromTag;

        HelpCommand helpCmd = new HelpCommand(commandMap, contentDiv);
        PlayCommand playCmd = new PlayCommand(grid);
        commandMap.put("?", helpCmd);
        commandMap.put("help", helpCmd);
        commandMap.put("fav", new FavCommand(grid, favoris));
        commandMap.put("f", new FavCommand(grid, favoris));
        commandMap.put("sort replies", new SortRepliesCommand(grid));
        commandMap.put("h", new HashtagCommand(grid, contentDiv, tagFetcher));
        commandMap.put("hashtag", new HashtagCommand(grid, contentDiv, tagFetcher));
        commandMap.put("c", new ContentSearchCommand(grid, contentDiv));
        commandMap.put("rand", new RandCommand(grid));
        commandMap.put("random", new RandCommand(grid));
        commandMap.put("reset", new ResetCommand(grid, () -> fetchPostsFromTag("info")));
        commandMap.put("top", new TopCommand(grid));
        commandMap.put("bottom", new BottomCommand(grid));
        commandMap.put("select", new SelectByIdCommand(grid));
        commandMap.put("taglist", new TagListCommand(grid, contentDiv));
        commandMap.put("play", playCmd);
        commandMap.put("stop", new StopCommand(playCmd));
        commandMap.put("g", new GotoCommand(grid));
        commandMap.put("goto", new GotoCommand(grid));
        commandMap.put("n", new NextCommand(grid));
        commandMap.put("next", new NextCommand(grid));
        commandMap.put("p", new PreviousCommand(grid));
        commandMap.put("previous", new PreviousCommand(grid));
        commandMap.put("r", new RepliesGreaterCommand(grid));
        commandMap.put("replies", new RepliesGreaterCommand(grid));
        commandMap.put("l", new LinkCommand(grid, contentDiv));
        commandMap.put("link", new LinkCommand(grid, contentDiv));

        prompt.addValueChangeListener(v -> {
            if (internalChange.get())
                return;
            String text = v.getValue().trim();
            if (text.isEmpty())
                return;

            PlayCommand.stop();

            if (commandMap.containsKey(text.split(" ")[0])) {
                commandMap.get(text.split(" ")[0]).execute(text);
            } else if (commandMap.containsKey(text)) {
                commandMap.get(text).execute(text);
            }

            internalChange.set(true);
            prompt.setValue("");
            internalChange.set(false);
        });

        var horizontalLayout = new HorizontalLayout();
        horizontalLayout.setSizeFull();
        grid.setHeight("100%");
        grid.setWidth("50%");
        contentDiv.setHeight("100%");
        contentDiv.setWidth("50%");
        horizontalLayout.add(grid, contentDiv);
        add(horizontalLayout);
        promptContainer.setWidth("100%");
        promptContainer.setPadding(false);
        promptContainer.setSpacing(false);
        promptContainer.setMargin(false);
        add(promptContainer);
        setFlexGrow(1, horizontalLayout);
        setFlexGrow(0, promptContainer);

        grid.addSelectionListener(event -> selectItemListener(grid, contentDiv, event));
    }

    private void addIndexColumn(Grid<MastodonPost> grid) {
        grid.addColumn(post -> {
            List<MastodonPost> items = grid.getListDataView().getItems().toList();
            return items.indexOf(post) + 1;
        }).setAutoWidth(true);
    }

    private void addContentColumn(Grid<MastodonPost> grid) {
        grid.addColumn(p -> StringUtils.left(Jsoup.parse(p.getContent()).text(), 150)).setAutoWidth(true);
    }

    public List<MastodonPost> fetchPostsFromTag(String tag) {
        if (StringUtils.isEmpty(tag))
            return List.of();
        try {
            var uri = new URIBuilder("https://mastodon.social/api/v1/timelines/tag/" + tag)
                    .addParameter("limit", "10")
                    .build();

            URL url = uri.toURL();
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            return Arrays.stream(mapper.readValue(response.toString(), MastodonPost[].class)).toList();
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    private void selectItemListener(Grid<MastodonPost> grid, Div contentDiv,
            com.vaadin.flow.data.selection.SelectionEvent<Grid<MastodonPost>, MastodonPost> event) {
        grid.getDataProvider().fetch(new Query<>()).forEach(p -> grid.setDetailsVisible(p, false));
        event.getFirstSelectedItem().ifPresent(post -> {
            VerticalLayout container = new VerticalLayout();
            Div postContent = new Div();
            postContent.getElement().setProperty("innerHTML", post.getContent());
            container.add(postContent);
            contentDiv.removeAll();
            contentDiv.add(container);
            grid.setDetailsVisible(post, true);
        });
    }
};

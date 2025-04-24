package antix.views.main;

import antix.components.ApercuPost;
import antix.factory.CommandFactory;
import antix.model.MastodonPost;
import antix.views.main.commands.Command;
import antix.views.main.commands.PlayCommand;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.utils.URIBuilder;
import org.jsoup.Jsoup;
import com.vaadin.flow.data.provider.Query;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;

@PageTitle("NoelMusk - Test pour la bande")
@Route("")
public class MainView extends VerticalLayout {
    private final Grid<MastodonPost> grid;
    private final Div contentDiv;
    private final List<String> commandesTapees = new ArrayList<>();


    public MainView() {
        var prompt = new TextField();
        prompt.setId("prompt-field");
        prompt.setWidth("100%");

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
                            hole.style.zIndex = '10000';
                            document.body.appendChild(hole);
                            promptField.style.zIndex = '10001';
                            promptField.style.position = 'relative';
                            promptField.style.pointerEvents = 'auto';
                        }
                    }, 100);
                """);

        setSizeFull();
        setAlignItems(FlexComponent.Alignment.CENTER);

        this.grid = new Grid<>(MastodonPost.class, false);
        this.contentDiv = new Div();
        grid.setSelectionMode(Grid.SelectionMode.SINGLE);
        contentDiv.setWidthFull();

        addIndexColumn(grid);
        addContentColumn(grid);

        List<MastodonPost> favoris = new ArrayList<>();
        PostSelector selector = this::selectAndDisplay;

        Map<String, Command> commandMap = CommandFactory.build(
                grid,
                contentDiv,
                selector,
                favoris,
                () -> fetchPostsFromTag("info"),
                this::fetchPostsFromTag,
                commandesTapees);

        PlayCommand playCmd = (PlayCommand) commandMap.get("play");

        prompt.addValueChangeListener(v -> {
            String text = v.getValue().trim();
            if (text.isEmpty())
                return;

            playCmd.stop();
            //récupère les commande pour l'historique      
            commandesTapees.add(text);

            commandMap.getOrDefault(text.split(" ")[0], commandMap.get(text))
                    .execute(text);    

            prompt.setValue("");
        });

        // Créer un HorizontalLayout pour contenir la grid et le contenu
        var horizontalLayout = new HorizontalLayout();
        horizontalLayout.setSizeFull();

        // Ajuster la taille de la grid
        grid.setHeight("100%");
        grid.setWidth("50%");

        // Ajuster le contentDiv
        contentDiv.setHeight("100%");
        contentDiv.setWidth("50%");

        // Ajouter les composants au layout horizontal
        horizontalLayout.add(grid, contentDiv);

        // Remplacer les add() individuels par l'ajout du layout horizontal
        add(horizontalLayout);

        // Ajouter le prompt en bas
        var promptContainer = new VerticalLayout(prompt);
        promptContainer.setWidth("100%");
        promptContainer.setPadding(false);
        promptContainer.setSpacing(false);
        promptContainer.setMargin(false);
        add(promptContainer);

        // Ajuster les flex grow
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
        grid.addComponentColumn(post -> {
            ApercuPost apercuPost = new ApercuPost(post);
            return apercuPost;
        }).setAutoWidth(true);
    }

    public List<MastodonPost> fetchPostsFromTag(String tag) {
        if (StringUtils.isEmpty(tag))
            return List.of();

        try {
            var uri = new URIBuilder("https://mastodon.social/api/v1/timelines/tag/" + tag)
                    .addParameter("limit", "40")
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
        event.getFirstSelectedItem().ifPresent(this::selectAndDisplay);
    }

    public void selectAndDisplay(MastodonPost post) {
        grid.select(post);
        contentDiv.removeAll();

        if (post != null) {
            VerticalLayout container = new VerticalLayout();
            Div postContent = new Div();
            postContent.getElement().setProperty("innerHTML", post.getContent());
            container.add(postContent);
            contentDiv.add(container);
            grid.setDetailsVisible(post, true);
        }
    }
};

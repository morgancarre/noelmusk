package antix.views.main;

import antix.factory.CommandFactory;
import antix.model.SocialMediaPost;
import antix.service.MastodonService;
import antix.service.RedditService;
import antix.utils.GridUtils;
import antix.components.PlatformBadge;
import antix.views.main.commands.Command;
import antix.views.main.commands.PlayCommand;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
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
import com.vaadin.flow.data.provider.Query;

import java.util.*;
import java.util.function.Supplier;
import java.util.logging.Logger;
import java.util.logging.Level;

@PageTitle("NoelMusk - Test pour la bande")
@Route("")
public class MainView extends VerticalLayout {
    
    private static final Logger logger = Logger.getLogger(MainView.class.getName());
    
    private final Grid<SocialMediaPost> grid;
    private final Div contentDiv;
    private final List<String> commandesTapees = new ArrayList<>();
    
    private final RedditService redditService = new RedditService();
    private final MastodonService mastodonService = new MastodonService(); 
    
    public MainView() {
        var prompt = new TextField();
        prompt.setId("prompt-field");
        prompt.setWidth("100%");
        
        // Code JavaScript existant...
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
        
        // Code des vagues existant...
        Div backgroundWaves = new Div();
        backgroundWaves.getElement().setProperty("innerHTML", """
            <svg class="waves" xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink"
                viewBox="0 0 150 100" preserveAspectRatio="none" shape-rendering="auto">
              <defs>
                <path id="gentle-wave" d="M-160 44c30 0 58-18 88-18s 58 18 88 18
                  58-18 88-18 58 18 88 18 v44h-352z" />
              </defs>
              <g class="parallax">
                <use xlink:href="#gentle-wave" x="48" y="0" fill="rgba(37, 40, 57,0.2)" />
                <use xlink:href="#gentle-wave" x="48" y="2" fill="rgba(37, 40, 57,0.5)" />
                <use xlink:href="#gentle-wave" x="48" y="-1" fill="rgba(37, 40, 57,0.3)" />
                <use xlink:href="#gentle-wave" x="48" y="0" fill="#252839" />
              </g>
            </svg>
        """);
        backgroundWaves.addClassName("background-waves");

        getElement().insertChild(0, backgroundWaves.getElement());

        // Grid setup avec GridUtils
        this.grid = new Grid<>(SocialMediaPost.class);
        this.grid.addClassName("custom-grid");
        grid.setSelectionMode(Grid.SelectionMode.SINGLE);
        grid.setColumns(); // Efface les colonnes par défaut

        this.contentDiv = new Div();
        contentDiv.setWidthFull();

        // ✅ Utilisation de GridUtils
        GridUtils.addIndexColumn(grid);
        GridUtils.addPlatformColumn(grid);
        GridUtils.addPostInfoColumn(grid);

        List<SocialMediaPost> favoris = new ArrayList<>();
        
        PostSelector selector = this::selectAndDisplay;

        Supplier<List<SocialMediaPost>> defaultSupplier = () -> this.fetchPostsFromTag("programming");

        Map<String, Command> commandMap = CommandFactory.build(
                grid,
                contentDiv,
                selector,
                favoris,
                defaultSupplier,
                this::fetchPostsFromTag,
                commandesTapees);

        PlayCommand playCmd = (PlayCommand) commandMap.get("play");
        
        prompt.addValueChangeListener(v -> {
            String text = v.getValue().trim();
            if (text.isEmpty())
                return;

            playCmd.stop();
            commandesTapees.add(text);

            String commandKey = text.split(" ")[0];
            Command command = commandMap.getOrDefault(commandKey, null);

            if (command != null) {
                command.execute(text);
            } else {
                Notification notification = Notification.show("Commande inconnue : \"" + commandKey + "\"", 1000, Notification.Position.TOP_END);
                notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
            }
            prompt.setValue("");
        });

        // Reste du layout existant...
        var horizontalLayout = new HorizontalLayout();
        horizontalLayout.setSizeFull();
        horizontalLayout.add(backgroundWaves, grid, contentDiv);
        horizontalLayout.getStyle().set("position", "relative");

        grid.setHeight("100%");
        grid.setWidth("50%");

        contentDiv.setHeight("100%");
        contentDiv.setWidth("50%");

        horizontalLayout.add(grid, contentDiv);

        add(horizontalLayout);

        var promptContainer = new VerticalLayout(prompt);
        promptContainer.setWidth("100%");
        promptContainer.setPadding(false);
        promptContainer.setSpacing(false);
        promptContainer.setMargin(false);
        add(promptContainer);

        setFlexGrow(1, horizontalLayout);
        setFlexGrow(0, promptContainer);

        grid.addSelectionListener(event -> selectItemListener(grid, contentDiv, event));
    }

    // Méthodes existantes restent identiques...
    public List<SocialMediaPost> fetchPostsFromTag(String tag) {
        return fetchPostsFromTag(tag, 40);
    }

    public List<SocialMediaPost> fetchPostsFromTag(String tag, int maxPerService) {
        if (StringUtils.isEmpty(tag)) {
            return List.of();
        }

        List<SocialMediaPost> allPosts = new ArrayList<>();
        
        try {
            System.out.println("🔍 Tentative de récupération des posts Reddit pour le tag: " + tag);
            List<SocialMediaPost> redditPosts = redditService.fetchPostsFromTag(tag, maxPerService);
            System.out.println("✅ Posts Reddit récupérés: " + redditPosts.size() + "/" + maxPerService + " max");
            allPosts.addAll(redditPosts);
            
            System.out.println("🔍 Tentative de récupération des posts Mastodon pour le tag: " + tag);
            List<SocialMediaPost> mastodonPosts = mastodonService.fetchPostsFromTag(tag, maxPerService);
            System.out.println("✅ Posts Mastodon récupérés: " + mastodonPosts.size() + "/" + maxPerService + " max");
            allPosts.addAll(mastodonPosts);
            
            allPosts.sort((post1, post2) -> post2.getCreatedAt().compareTo(post1.getCreatedAt()));
            
            System.out.println("🎯 TOTAL FINAL: " + allPosts.size() + " posts");
            System.out.println("📊 Répartition: " + redditPosts.size() + " Reddit + " + mastodonPosts.size() + " Mastodon");
            
            updateGrid(allPosts);
            
            return allPosts;
            
        } catch (Exception e) {
            System.out.println("❌ ERREUR lors de la récupération des posts: " + e.getMessage());
            logger.log(Level.SEVERE, "Erreur lors de la récupération des posts", e);
            
            Notification notification = Notification.show(
                "Erreur lors de la récupération des posts : " + e.getMessage(), 
                3000, 
                Notification.Position.TOP_END
            );
            notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
            
            return new ArrayList<>();
        }
    }

    public void updateGrid(List<SocialMediaPost> posts) {
        System.out.println("🔄 Mise à jour du grid avec " + posts.size() + " posts");
        grid.setItems(posts);
        grid.getDataProvider().refreshAll();
        
        if (!posts.isEmpty()) {
            System.out.println("✅ Grid mis à jour avec succès");
        } else {
            System.out.println("⚠️ Aucun post à afficher dans le grid");
        }
    }

    private void selectItemListener(Grid<SocialMediaPost> grid, Div contentDiv,
            com.vaadin.flow.data.selection.SelectionEvent<Grid<SocialMediaPost>, SocialMediaPost> event) {
        grid.getDataProvider().fetch(new Query<>()).forEach(p -> grid.setDetailsVisible(p, false));
        event.getFirstSelectedItem().ifPresent(this::selectAndDisplay);
    }

public void selectAndDisplay(SocialMediaPost post) {
    grid.select(post);
    contentDiv.removeAll();

    if (post != null) {
        VerticalLayout container = new VerticalLayout();
        container.setWidthFull();
        container.setSpacing(false);

        PlatformBadge largeBadge = new PlatformBadge(post, PlatformBadge.Size.LARGE, PlatformBadge.Style.BADGE);
        HorizontalLayout platformHeader = new HorizontalLayout(largeBadge);
        container.add(platformHeader);

        HorizontalLayout header = new HorizontalLayout();
        header.setWidthFull();
        header.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
        header.setAlignItems(FlexComponent.Alignment.START);

        VerticalLayout userInfo = new VerticalLayout();
        userInfo.setSpacing(false);
        userInfo.setPadding(false);

        Div displayNameDiv = new Div();
        displayNameDiv.getStyle().set("font-weight", "bold");
        displayNameDiv.setText(post.getDisplayName());

        Div platformDiv = new Div();
        platformDiv.getStyle().set("color", "orange");
        platformDiv.setText(post.getPlatformInfo());

        Div dateDiv = new Div();
        dateDiv.getStyle().set("color", "gray");
        dateDiv.getStyle().set("font-size", "0.9em");
        dateDiv.getStyle().set("margin-top", "4px");
        dateDiv.setText("📅 " + post.getFormattedDate());

        userInfo.add(displayNameDiv, platformDiv, dateDiv); 

        Div scoreDiv = new Div();
        scoreDiv.getStyle().set("font-size", "0.8em");
        scoreDiv.getStyle().set("color", "gray");
        scoreDiv.setText(post.getScoreText());

        header.add(userInfo, scoreDiv);
        container.add(header);
        
        Div postContent = new Div();
        postContent.getElement().setProperty("innerHTML", post.getContent());
        container.add(postContent);
        
        Div engagementDiv = new Div();
        engagementDiv.setText(post.getEngagementText());
        container.add(engagementDiv);

        contentDiv.removeAll();
        contentDiv.add(container);
        grid.setDetailsVisible(post, true);
    }
}
}
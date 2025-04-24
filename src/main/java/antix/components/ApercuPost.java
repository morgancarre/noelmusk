package antix.components;

import antix.model.MastodonPost;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.FlexComponent;

import java.time.format.DateTimeFormatter;

public class ApercuPost extends Div {

    private MastodonPost post;

    public ApercuPost(MastodonPost post) {
        this.post = post;
        addClassName("apercu-post");

        // Titre de l'auteur
        H3 authorName = new H3(post.getAccount().getDisplayName());
        authorName.addClassName("author-name");

        // Nom d'utilisateur et @
        String username = post.getAccount().getUsername();
        Span userTag = new Span("@" + username);  // Affiche le @username
        userTag.addClassName("user-tag");
        userTag.getStyle().set("font-size", "small"); // Style pour afficher le tag en petit texte
        userTag.getStyle().set("color", "#FFF"); // Couleur plus claire pour le tag

        // Appliquer les styles pour limiter la taille du titre
        authorName.getStyle().set("white-space", "nowrap");  // Empêche le texte de se diviser en plusieurs lignes
        authorName.getStyle().set("overflow", "hidden");  // Cache le texte débordant
        authorName.getStyle().set("text-overflow", "ellipsis");  // Ajoute "..." si le texte est trop long
        authorName.setWidth("100%");  // Force l'élément à occuper toute la largeur disponible du parent

        // Contenu du post (limité à 150 caractères, HTML inclus)
        String contentPreview = post.getContent().length() > 150 ? post.getContent().substring(0, 150) + "..." : post.getContent();
        Span postContent = new Span();
        postContent.getElement().setProperty("innerHTML", contentPreview); // Cette ligne permet de rendre du contenu HTML
        postContent.addClassName("post-content");

        // Ajouter des styles pour gérer le texte et son débordement
        postContent.getStyle().set("white-space", "normal");  // Permet au texte de se diviser en plusieurs lignes
        postContent.getStyle().set("overflow", "hidden");  // Cache tout débordement
        postContent.getStyle().set("word-wrap", "break-word");  // Casse les mots longs pour éviter le débordement
        postContent.setWidth("100%");  // Assure que l'élément occupe toute la largeur disponible du parent

        // Date et heure du post
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        String formattedDate = post.getCreatedAt().format(formatter); // Formate la date de création

        Span postDate = new Span("Posted on: " + formattedDate);
        postDate.addClassName("post-date");
        postDate.getStyle().set("font-size", "small"); // Style pour afficher la date en petit texte
        postDate.getStyle().set("color", "#FFF"); // Couleur plus claire pour la date

        // Layout horizontal pour aligner l'auteur (nom et @)
        HorizontalLayout authorLayout = new HorizontalLayout(authorName, userTag);
        authorLayout.setMaxWidth("300px");
        authorLayout.setAlignItems(FlexComponent.Alignment.CENTER);

        // Ajouter tous les éléments dans un VerticalLayout
        VerticalLayout layout = new VerticalLayout(authorLayout, postContent, postDate);
        layout.setSpacing(false);
        layout.setPadding(false);
        layout.setMaxWidth("300px");  // Limite la largeur du post (ajuster la largeur ici)
        layout.setWidth("100%");  // Assure que le verticalLayout prend 100% de la largeur disponible

        add(layout);
    }
}

package antix.utils;

import com.vaadin.flow.component.html.Div;

/**
 * Utilitaire pour afficher des messages dans l'interface.
 * Centralise les messages d'erreur, d'information ou de succès dans la zone
 * feedbackDiv.
 */
public class FeedbackUtils {

    /**
     * Affiche un message dans le feedbackDiv.
     *
     * @param feedbackDiv Zone d'affichage.
     * @param message     Message à afficher.
     */
    public static void showMessage(Div feedbackDiv, String message) {
        feedbackDiv.removeAll();
        Div msg = new Div();
        msg.setText("ℹ️ " + message);
        msg.getStyle().set("color", "blue");
        feedbackDiv.add(msg);
    }

    /**
     * Affiche une erreur.
     *
     * @param feedbackDiv Zone d'affichage.
     * @param message     Message d'erreur.
     */
    public static void showError(Div feedbackDiv, String message) {
        feedbackDiv.removeAll();
        Div msg = new Div();
        msg.setText("❌ " + message);
        msg.getStyle().set("color", "red");
        feedbackDiv.add(msg);
    }

    /**
     * Affiche un message de succès.
     *
     * @param feedbackDiv Zone d'affichage.
     * @param message     Message de confirmation.
     */
    public static void showSuccess(Div feedbackDiv, String message) {
        feedbackDiv.removeAll();
        Div msg = new Div();
        msg.setText("✅ " + message);
        msg.getStyle().set("color", "green");
        feedbackDiv.add(msg);
    }
}

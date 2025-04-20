package antix.utils;

import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.notification.NotificationVariant;

/**
 * Utilitaire pour afficher des notifications de feedback dans l'interface.
 */
public class FeedbackUtils {

    /**
     * Affiche un message.
     *
     * @param message     Message à afficher.
     */
    public static void showMessage(String message) {
        Notification notif = Notification.show("ℹ️ " + message);
        notif.addThemeVariants(NotificationVariant.LUMO_PRIMARY);
        notif.setPosition(Position.TOP_END);
    }

    /**
     * Affiche une erreur.
     *
     * @param message     Message d'erreur.
     */
    public static void showError(String message) {
        Notification notif = Notification.show("❌ " + message);
        notif.addThemeVariants(NotificationVariant.LUMO_ERROR);
        notif.setPosition(Position.TOP_END);
    }

    /**
     * Affiche un message de succès.
     *
     * @param message     Message de confirmation.
     */
    public static void showSuccess(String message) {
        Notification notif = Notification.show("✅ " + message);
        notif.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
        notif.setPosition(Position.TOP_END);
    }
}

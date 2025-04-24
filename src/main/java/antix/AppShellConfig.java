package antix;

import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;

import org.springframework.stereotype.Component;

/**
 * Configuration de l'application Vaadin : Push activé ici.
 * Évite les erreurs de démarrage si on mettait @Push sur une vue.
 */
@Push
@Component
@Theme(value = "antix", variant = Lumo.DARK)
public class AppShellConfig implements AppShellConfigurator {
}

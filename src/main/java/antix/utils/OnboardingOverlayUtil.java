package antix.utils;

import antix.views.main.commands.Command;
import java.util.*;

public class OnboardingOverlayUtil {

    public static String buildOnboardingJs(Collection<Command> commands) {
        StringBuilder commandsJsArray = new StringBuilder();
        Set<String> seenDescriptionsJs = new HashSet<>();
        for (Command cmd : commands) {
            String desc = cmd.getDescription()
                .replace("\\", "\\\\")
                .replace("`", "\\`")
                .replace("'", "\\'")
                .replace("\"", "\\\"");
            String alias = cmd.getAliases().get(0)
                .replace("\\", "\\\\")
                .replace("'", "\\'")
                .replace("\"", "\\\"");
            if (seenDescriptionsJs.add(desc)) {
                if (commandsJsArray.length() > 0) commandsJsArray.append(",\n");
                commandsJsArray.append("{alias: '").append(alias).append("', desc: `").append(desc).append("`}");
            }
        }

        return
            "if (!window._noelmusk_onboarding) {" +
            "window._noelmusk_onboarding = true;" +
            "const overlay = document.createElement('div');" +
            "overlay.id = 'onboarding-overlay';" +
            "overlay.style.position = 'fixed';" +
            "overlay.style.top = 0;" +
            "overlay.style.left = 0;" +
            "overlay.style.width = '100vw';" +
            "overlay.style.height = '100vh';" +
            "overlay.style.background = 'rgba(37,40,57,0.97)';" +
            "overlay.style.zIndex = '30000';" +
            "overlay.style.display = 'flex';" +
            "overlay.style.flexDirection = 'column';" +
            "overlay.style.alignItems = 'center';" +
            "overlay.style.justifyContent = 'center';" +
            "overlay.style.padding = '0';" +
            "overlay.style.margin = '0';" +
            "overlay.style.boxSizing = 'border-box';" +
            "overlay.style.overflow = 'hidden';" +
            "overlay.style.pointerEvents = 'auto';" + // Permet de bloquer tous les clics
            "overlay.addEventListener('mousedown', e => e.preventDefault());" + // Bloque le clic
            "overlay.addEventListener('click', e => e.preventDefault());" +     // Bloque le clic
            "const bubble = document.createElement('div');" +
            "bubble.style.background = 'rgba(37,40,57,1)';" +
            "bubble.style.borderRadius = '32px';" +
            "bubble.style.padding = '32px 2vw 32px 2vw';" +
            "bubble.style.boxShadow = '0 12px 48px rgba(0,0,0,0.25)';" +
            "bubble.style.maxWidth = '900px';" +
            "bubble.style.width = '98vw';" +
            "bubble.style.maxHeight = '90vh';" +
            "bubble.style.overflow = 'auto';" +
            "bubble.style.textAlign = 'left';" +
            "bubble.style.fontSize = '1.08em';" +
            "bubble.style.color = '#fff';" +
            "bubble.style.margin = '24px auto';" +
            "bubble.innerHTML = `" +
            "<div style='font-size:2em; margin-bottom:16px; text-align:center;'>Bienvenue et voici toutes commandes :</div>" +
            "<div style='margin-bottom:18px; font-size:1em; text-align:center;'>(Utilisez uniquement les flèches ↑ et ↓ pour faire défiler la liste. Appuyez sur <b>Entrée</b> pour accéder au site.)</div>" +
            "<div style='overflow:auto; max-height:70vh;'>" +
            "<table id='onboarding-commands-table' style='width:100%; border-collapse:collapse; margin-bottom:18px; background:rgba(37,40,57,1); color:#fff; table-layout:fixed; word-break:break-word;'>" +
            "<colgroup><col style='width: 180px;'><col style='width:auto;'></colgroup>" +
            "<thead><tr><th style='text-align:left; padding:8px; background:rgba(37,40,57,0.95); border-radius:12px 0 0 12px; font-size:1em; color:#fff;'>Commande</th><th style='text-align:left; padding:8px; background:rgba(37,40,57,0.95); border-radius:0 12px 12px 0; font-size:1em; color:#fff;'>Description</th></tr></thead>" +
            "<tbody></tbody></table></div>" +
            "`;" +
            "overlay.appendChild(bubble);" +
            "document.body.appendChild(overlay);" +
            "const commands = [" + commandsJsArray + "];" +
            "const tableBody = bubble.querySelector('#onboarding-commands-table tbody');" +
            "commands.forEach((cmd, idx) => {" +
            "  const row = document.createElement('tr');" +
            "  const cmdCell = document.createElement('td');" +
            "  cmdCell.style.padding = '8px 10px';" +
            "  cmdCell.style.fontWeight = 'bold';" +
            "  cmdCell.style.background = (idx % 2 === 0) ? 'rgba(37,40,57,0.93)' : 'rgba(44,47,66,0.93)';" +
            "  cmdCell.style.borderBottom = '1px solid #444';" +
            "  cmdCell.style.color = '#fff';" +
            "  cmdCell.style.wordBreak = 'break-all';" +
            "  cmdCell.style.fontSize = '1em';" +
            "  cmdCell.innerText = '/' + cmd.alias;" +
            "  const descCell = document.createElement('td');" +
            "  descCell.style.padding = '8px 10px';" +
            "  descCell.style.background = (idx % 2 === 0) ? 'rgba(37,40,57,0.93)' : 'rgba(44,47,66,0.93)';" +
            "  descCell.style.borderBottom = '1px solid #444';" +
            "  descCell.style.color = '#fff';" +
            "  descCell.style.wordBreak = 'break-word';" +
            "  descCell.style.fontSize = '1em';" +
            "  descCell.innerText = cmd.desc;" +
            "  row.appendChild(cmdCell);" +
            "  row.appendChild(descCell);" +
            "  tableBody.appendChild(row);" +
            "});" +
            "bubble.style.pointerEvents = 'none';" +
            "let scrollPos = 0;" +
            "const scrollStep = 48;" +
            "const scrollable = bubble.querySelector('div[style*=\"overflow:auto\"]');" +
            "overlay.tabIndex = 0;" +
            "overlay.focus();" +
            "const keyHandler = (e) => {" +
            "  if (e.key === 'ArrowDown') {" +
            "    e.preventDefault();" +
            "    if (scrollable) scrollable.scrollTop += scrollStep;" +
            "  } else if (e.key === 'ArrowUp') {" +
            "    e.preventDefault();" +
            "    if (scrollable) scrollable.scrollTop -= scrollStep;" +
            "  } else if (e.key === 'Enter') {" +
            "    e.preventDefault();" +
            "    overlay.remove();" +
            "    document.removeEventListener('keydown', keyHandler);" +
            "  } else {" +
            "    e.preventDefault();" +
            "  }" +
            "};" +
            "document.addEventListener('keydown', keyHandler);" +
            // Empêche le scroll souris/tactile
            "const preventScroll = e => { e.preventDefault(); };" +
            "overlay.addEventListener('wheel', preventScroll, { passive: false });" +
            "overlay.addEventListener('touchmove', preventScroll, { passive: false });" +
            "}";
    }
}

package com.arkflame.authmepremium.listeners;

import com.arkflame.authmepremium.AuthMePremiumPlugin;
import com.arkflame.authmepremium.events.PremiumPostLoginEvent;
import com.arkflame.authmepremium.utils.HandlerReflectionUtil;

import fr.xephi.authmebungee.AuthMeBungee;
import fr.xephi.authmebungee.data.AuthPlayer;
import fr.xephi.authmebungee.services.AuthPlayerManager;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

public class PostLoginListener implements Listener {
    public void hookAuthMeBungee(String name)
            throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException {
        Plugin plugin = BungeeCord.getInstance().getPluginManager().getPlugin("AuthMeBungee");
        if (plugin instanceof AuthMeBungee) {
            AuthMeBungee authMeBungee = (AuthMeBungee) plugin;
            AuthPlayerManager authPlayerManager = HandlerReflectionUtil.getFieldValue(authMeBungee,
                    "authPlayerManager");
            AuthPlayer authPlayer = new AuthPlayer(name);
            authPlayer.setLogged(true);
            authPlayerManager.addAuthPlayer(authPlayer);
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPostLogin(PostLoginEvent event) {
        ProxiedPlayer player = event.getPlayer();

        if (player != null) {
            String name = player.getName();
            Boolean isPremium = AuthMePremiumPlugin.getDataProvider().getPremium(name);

            if (isPremium != null && isPremium) {
                // Hook into AuthMeBungee
                try {
                    hookAuthMeBungee(name);
                } catch (NoSuchFieldException | SecurityException | IllegalArgumentException
                        | IllegalAccessException e) {
                    e.printStackTrace();
                }

                BungeeCord.getInstance().getPluginManager().callEvent(new PremiumPostLoginEvent(event));
            }
        }
    }
}

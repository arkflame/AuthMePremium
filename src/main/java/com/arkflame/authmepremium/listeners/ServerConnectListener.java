package com.arkflame.authmepremium.listeners;

import com.arkflame.authmepremium.AuthMePremiumPlugin;

import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class ServerConnectListener implements Listener {

    @EventHandler
    public void onServerConnect(ServerConnectEvent event) {
        // Get the player
        ProxiedPlayer player = event.getPlayer();

        // Player had no server before
        if (player.getServer() == null) {
            Boolean isPremium = AuthMePremiumPlugin.getDataProvider().getPremium(player.getName());
            if (isPremium != null && isPremium) {
                String targetServer = determineTargetServer();
                if (targetServer != null) {
                    event.setTarget(BungeeCord.getInstance().getServerInfo(targetServer));
                }
            }
        }
    }

    private String determineTargetServer() {
        return AuthMePremiumPlugin.getConfig().getString("target-server");
    }
}

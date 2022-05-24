package me.l2x9.chagepass.listeners;

import fr.xephi.authme.api.v3.AuthMeApi;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.bukkit.plugin.messaging.PluginMessageRecipient;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;

public class PluginMessageHandler implements PluginMessageListener {
    private AuthMeApi authme;
    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        if (authme == null) authme = AuthMeApi.getInstance();
        if (!channel.equals("auth:channel")) return;
        try {
            ByteArrayInputStream stream = new ByteArrayInputStream(message);
            DataInputStream in = new DataInputStream(stream);
            String newPass = in.readUTF();
            String oldPass = in.readUTF();
            String playerName = in.readUTF();
            if (authme.checkPassword(playerName, oldPass)) {
                authme.changePassword(playerName, newPass);
                player.sendMessage(ChatColor.GREEN + "Password changed successfully");
            } else player.sendMessage(ChatColor.RED + "The old password you entered is incorrect");
            stream.close();
            in.close();
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }
}

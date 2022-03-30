package me.l2x9.chagepass.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;

public class PluginMessageHandler implements PluginMessageListener {
    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        if (!channel.equals("auth:channel")) return;
        try {
            ByteArrayInputStream stream = new ByteArrayInputStream(message);
            DataInputStream in = new DataInputStream(stream);
            String newPass = in.readUTF();
            String playerName = in.readUTF();
            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "authme changepass " + playerName + " " + newPass);
            stream.close();
            in.close();
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }
}

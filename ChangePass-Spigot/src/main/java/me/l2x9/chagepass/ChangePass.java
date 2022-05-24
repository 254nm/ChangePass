package me.l2x9.chagepass;

import me.l2x9.chagepass.listeners.PluginMessageHandler;
import org.bukkit.plugin.java.JavaPlugin;

public final class ChangePass extends JavaPlugin {

    @Override
    public void onEnable() {
        getServer().getMessenger().registerIncomingPluginChannel(this, "auth:channel", new PluginMessageHandler());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}

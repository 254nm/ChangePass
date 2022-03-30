package me.l2x9.changepass;

import me.l2x9.changepass.commands.ChangePassCommand;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.MissingResourceException;

public final class ChangePass extends Plugin {
    private Configuration config;

    @Override
    public void onEnable() {
        genConfig();
        getProxy().registerChannel("auth:channel");
        getProxy().getPluginManager().registerCommand(this, new ChangePassCommand(this));
    }
    private void genConfig() {
        try {
            if (!getDataFolder().exists()) getDataFolder().mkdirs();
            File configFile = new File(getDataFolder(), "config.yml");
            if (!configFile.exists()) {
                InputStream stream = getClass().getResourceAsStream("/config.yml");
                if (stream == null)
                    throw new MissingResourceException("Could not find default config.yml in the jar", getClass().getName(), "config.yml");
                Files.copy(stream, configFile.toPath());
            }
            config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(configFile);
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public Configuration getConfig() {
        return config;
    }
}

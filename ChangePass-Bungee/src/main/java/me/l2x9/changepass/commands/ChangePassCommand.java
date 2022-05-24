package me.l2x9.changepass.commands;

import me.l2x9.changepass.ChangePass;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.util.concurrent.TimeUnit;

public class ChangePassCommand extends Command {
    private final ServerInfo auth;
    private final ServerInfo main;
    private final ChangePass plugin;

    public ChangePassCommand(ChangePass plugin) {
        super("changepass");
        this.plugin = plugin;
        main = ProxyServer.getInstance().getServerInfo(plugin.getConfig().getString("MainServerName"));
        auth = ProxyServer.getInstance().getServerInfo(plugin.getConfig().getString("AuthServerName"));
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (sender instanceof ProxiedPlayer) {
            ProxiedPlayer player = (ProxiedPlayer) sender;
            if (player.getServer().getInfo().equals(main)) {
                if (args.length == 2) {
                    String newPassword = args[0];
                    if (newPassword.contains(" ")) {
                        player.sendMessage(new TextComponent(ChatColor.RED + "The new password cannot contain spaces"));
                        return;
                    }
                    try {
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        DataOutputStream out = new DataOutputStream(baos);
                        out.writeUTF(newPassword);
                        out.writeUTF(args[1]);
                        out.writeUTF(player.getName());
                        out.flush();
                        player.connect(auth);
                        auth.sendData("auth:channel", baos.toByteArray());
                        ProxyServer.getInstance().getScheduler().schedule(plugin, () -> {
                            player.connect(main);
                            player.sendMessage(new TextComponent(ChatColor.GREEN + "Password changed successfully"));
                        }, 3, TimeUnit.SECONDS);
                        out.close();
                        baos.close();
                    } catch (Throwable t) {
                        t.printStackTrace();
                    }
                } else player.sendMessage(new TextComponent(ChatColor.RED + "/changepass <oldPassword> <newPassword>"));
            } else player.sendMessage(new TextComponent(ChatColor.RED + "You must be connected to the main server"));
        }
    }
}

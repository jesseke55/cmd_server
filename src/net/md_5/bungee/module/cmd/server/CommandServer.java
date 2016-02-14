package net.md_5.bungee.module.cmd.server;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ClickEvent.Action;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.HoverEvent.Action;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.connection.Server;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;

public class CommandServer
        extends Command
        implements TabExecutor
{
    public CommandServer()
    {
        super("server", "bungeecord.command.server", new String[0]);
    }

    public void execute(CommandSender sender, String[] args)
    {
        if (!(sender instanceof ProxiedPlayer)) {
            return;
        }
        ProxiedPlayer player = (ProxiedPlayer)sender;
        Map<String, ServerInfo> servers = ProxyServer.getInstance().getServers();
        if (args.length == 0)
        {
            player.sendMessage(ProxyServer.getInstance().getTranslation("current_server", new Object[] { player.getServer().getInfo().getName() }));
            TextComponent serverList = new TextComponent(ProxyServer.getInstance().getTranslation("server_list", new Object[0]));
            serverList.setColor(ChatColor.GOLD);
            boolean first = true;
            for (ServerInfo server : servers.values()) {
                if (server.canAccess(player))
                {

                    TextComponent serverTextComponent = new TextComponent(", " + server.getName());
                    int count = server.getPlayers().size();
                    serverTextComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(count + (count == 1 ? " player" : " players") + "\n").append("Click to connect to the server").italic(true).create()));

                    serverTextComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/server " + server.getName()));
                    serverList.addExtra(serverTextComponent);
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&2&lTEST &8\\xbb &7Connecting you to " + server.getName()));
                    first = false;
                }
            }
            player.sendMessage(serverList);
        }
        else
        {
            ServerInfo server = (ServerInfo)servers.get(args[0]);
            if (server == null) {
                player.sendMessage(ProxyServer.getInstance().getTranslation("no_server", new Object[0]));
            } else if (!server.canAccess(player)) {
                player.sendMessage(ProxyServer.getInstance().getTranslation("no_server_permission", new Object[0]));
            } else {
                player.connect(server);
            }
        }
    }

    public Iterable<String> onTabComplete(final CommandSender sender, final String[] args)
    {
        args.length > 1 ? Collections.EMPTY_LIST : Iterables.transform(Iterables.filter(ProxyServer.getInstance().getServers().values(), new Predicate()
        {
            private final String lower = args.length == 0 ? "" : args[0].toLowerCase();

            public boolean apply(ServerInfo input)
            {
                return (input.getName().toLowerCase().startsWith(this.lower)) && (input.canAccess(sender));
            }
        }), new Function()
        {
            public String apply(ServerInfo input)
            {
                return input.getName();
            }
        });
    }
}

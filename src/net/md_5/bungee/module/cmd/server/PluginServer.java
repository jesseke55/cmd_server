package net.md_5.bungee.module.cmd.server;

import net.md_5.bungee.api.plugin.Plugin;

public class PluginServer
        extends Plugin
{

    public void onEnable()
    {
        getLogger().info("This jar has been modified by jesseke55 / Headhunterz_ for testing some stuff out!");
        getProxy().getPluginManager().registerCommand(this, new CommandServer());
    }
}

package me.ijedi.backuputil;

import org.bukkit.plugin.java.JavaPlugin;

public class BackupUtilMain extends JavaPlugin {

    @Override
    public void onEnable(){
        getLogger().info("BackupUtil enabled!");
    }

    @Override
    public void onDisable(){
        getLogger().info("BackupUtil disabled!");
    }
}

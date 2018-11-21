package me.ijedi.backuputil;

import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public class BackupUtilMain extends JavaPlugin {

    private final String ENABLED = "backupsEnabled";
    private final String DIRECTORY = "backupDirectory";
    private final String FOLDERS = "foldersToBackup";
    private final String FILES = "filesToBackup";
    private final String DAYS_TO_KEEP = "daysToKeepBackups";

    public static JavaPlugin thisPlugin;
    public static boolean isEnabled;
    public static String backupDirectory;
    public static List<String> directories, files;
    public static int daysToKeepBackups = 3;

    @Override
    public void onEnable(){
        thisPlugin = this;
        loadConfig();
        getCommand(BackupCommand.BASE_COMMAND).setExecutor(new BackupCommand());
        getCommand(BackupCommand.BASE_COMMAND).setTabCompleter(new BackupCommand());
        getLogger().info("BackupUtil enabled!");
    }

    @Override
    public void onDisable(){
        getLogger().info("BackupUtil disabled!");
    }

    private void loadConfig(){
        this.saveDefaultConfig();

        isEnabled = this.getConfig().getBoolean(ENABLED);
        backupDirectory = this.getConfig().getString(DIRECTORY);
        directories = this.getConfig().getStringList(FOLDERS);
        files = this.getConfig().getStringList(FILES);
        daysToKeepBackups = this.getConfig().getInt(DAYS_TO_KEEP);
    }

}

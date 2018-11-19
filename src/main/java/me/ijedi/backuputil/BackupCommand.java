package me.ijedi.backuputil;

import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class BackupCommand implements TabExecutor {

    public static final String BASE_COMMAND = "backup";
    private final String BACKUP_PERM = "backup.use";
    private static boolean isBackupInProgress;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args){

        // Test code
        if(sender.hasPermission(BACKUP_PERM)){

            // Check if enabled
            if(!BackupUtilMain.isEnabled){
                sender.sendMessage(ChatColor.RED + "Backups are disabled!");
                return true;
            }

            String serverRoot = new File("").getAbsolutePath();
            //sender.sendMessage(serverRoot);

            if(BackupUtilMain.backupDirectory == null || BackupUtilMain.backupDirectory.length() == 0){
                sender.sendMessage(ChatColor.RED + "Backup directory is not set.");
                return true;
            } else if((BackupUtilMain.directories == null || BackupUtilMain.directories.size() == 0)
                    && (BackupUtilMain.files == null || BackupUtilMain.files.size() == 0)) {
                sender.sendMessage(ChatColor.RED + "There are no configured files to backup.");
                return true;
            }

            new BukkitRunnable(){
                @Override
                public void run(){
                    // Make sure we aren't doing two backups at once.
                    if(isBackupInProgress){
                        sender.sendMessage(ChatColor.RED + "There is already a backup running!");
                        return;
                    }

                    // Do the backup.
                    isBackupInProgress = true;
                    sender.sendMessage(ChatColor.GREEN + "Backup started.");
                    try{
                        List<String> files = new ArrayList<>();
                        if(BackupUtilMain.directories != null){
                            files.addAll(BackupUtilMain.directories);
                        }
                        if(BackupUtilMain.files != null){
                            files.addAll(BackupUtilMain.files);
                        }

                        BackupHelper helper = new BackupHelper();
                        String destZip = String.format("%s%s/%s", serverRoot, BackupUtilMain.backupDirectory, helper.getNewBackupName("Backup"));
                        helper.zipFiles(serverRoot, destZip, files, false);
                        sender.sendMessage(ChatColor.GREEN + "Backup complete.");
                    } catch (Exception e){
                        e.printStackTrace();
                        sender.sendMessage(ChatColor.RED + "Error completing backup. See console for details.");
                    } finally {
                        isBackupInProgress = false;
                    }
                    this.cancel();
                }
            }.runTaskAsynchronously(BackupUtilMain.thisPlugin);

            return true;
        } else {
            sender.sendMessage(ChatColor.RED + "You need permission to use this!");
            return true;
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        return null;
    }
}

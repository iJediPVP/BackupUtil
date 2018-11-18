package me.ijedi.backuputil;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;

import java.util.List;

public class BackupCommand implements TabExecutor {

    private static String BASE_COMMAND = "backup";

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args){

        //String path = get


        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        return null;
    }
}

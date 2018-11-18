package me.ijedi.backuputil;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class BackupTest {

    private final String serverRoot = "C:\\Servers\\Spigot 1.13.2\\";
    private final String backupFolder = "C:\\Servers\\Spigot 1.13.2\\backups";

    @Test
    public void testGetBackupName(){
        String name = BackupHelper.getNewBackupName("world");
        System.out.println(name);
    }

    @Test
    public void testBackupFolders(){
        List<String> folders = new ArrayList<>();
        folders.add(serverRoot + "world");
        folders.add(serverRoot + "world_nether");
        folders.add(serverRoot + "world_the_end");


    }

    @Test
    public void testBackupFile() throws Exception{
        String fileName = serverRoot + "spigot.yml";
        List<String> files = new ArrayList<>();
        files.add(fileName);
        BackupHelper.zipFiles(serverRoot, backupFolder + "\\testBackupFile.zip", files, true);
    }

    @Test
    public void testBackupFolder() throws Exception{
        List<String> files = new ArrayList<>();
        files.add(serverRoot + "\\world");
        files.add(serverRoot + "\\world_nether");
        files.add(serverRoot + "\\world_the_end");
        BackupHelper.zipFiles(serverRoot, backupFolder + "\\testBackupFolder.zip", files, true);
    }

    @Test
    public void testBackupFilesAndFolders() throws Exception{
        List<String> files = new ArrayList<>();
        files.add(serverRoot + "\\world");
        files.add(serverRoot + "\\world_nether");
        files.add(serverRoot + "\\world_the_end");
        files.add(serverRoot + "\\plugins");
        files.add(serverRoot + "spigot.yml");
        BackupHelper.zipFiles(serverRoot, backupFolder + "\\testBackupFilesAndFolders.zip", files, true);
    }

}

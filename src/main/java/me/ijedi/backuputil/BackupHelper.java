package me.ijedi.backuputil;

import org.apache.commons.io.FileExistsException;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class BackupHelper {

    public String getNewBackupName(String folderName){
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        folderName = String.format("%s-%s.zip", folderName, format.format(date));
        return folderName;
    }

    public void zipFiles(String basePath, String destPathStr, List<String> filesToZip, boolean overwrite) throws FileExistsException{
        // See if our destination exists
        File destFile = new File(destPathStr);
        if(destFile.exists()){
            if(overwrite){
                destFile.delete();
            } else {
                throw new FileExistsException(destPathStr);
            }

        } else {
            destFile.getParentFile().mkdirs();
            try{
                Files.createFile(Paths.get(destPathStr));
            } catch (IOException e){
                System.out.println(e.toString());
                return;
            }
        }

        FileOutputStream stream = null;
        ZipOutputStream zipStream = null;
        try{
            stream = new FileOutputStream(destFile);
            zipStream = new ZipOutputStream(stream);

            // Loop through the files
            for(String filePath : filesToZip){
                File file = new File(filePath);
                if(file.exists()){


                    if(file.isDirectory()){
                        // Handle directories
                        zipSubFolder(basePath, file, zipStream);

                    } else {
                        // Handle files
                        zipFile(basePath, file, zipStream);
                    }

                }
            }


        } catch (IOException e){
            System.out.println(e.toString());
        } finally {
            try{
                zipStream.finish();
                zipStream.close();
            } catch (IOException e){
                System.out.println(e.toString());
            }
        }

    }

    // Recursively zip a folder and all of its sub folders and files.
    private void zipSubFolder(String basePath, File directory, ZipOutputStream stream) throws IOException{
        for(File childFile : directory.listFiles()){

            if(childFile.isDirectory()){
                //String path = basePath + "/" + childFile.getName() + "/";
                String relativePath = new File(basePath).toURI().relativize(childFile.toURI()).getPath();
                stream.putNextEntry(new ZipEntry(relativePath));
                zipSubFolder(basePath, childFile, stream);
                stream.closeEntry();
            } else {
                zipFile(basePath, childFile, stream);
            }
        }
    }

    // Zips a single file
    private void zipFile(String basePath, File file, ZipOutputStream stream) throws IOException{
        Path path = Paths.get(file.getPath());
        String relativePath = new File(basePath).toURI().relativize(file.toURI()).getPath();
        ZipEntry entry = new ZipEntry(relativePath);
        stream.putNextEntry(entry);
        Files.copy(path, stream);
        stream.closeEntry();
    }


    public void purgeOldFiles(String baseDirectory, String backupPrefix, int daysToKeep){
        File backupDir = new File(baseDirectory);
        File[] backupFiles = backupDir.listFiles();
        List<File> filesToPurge = new ArrayList<>();

        LocalDate cutoffDate = LocalDate.now().minusDays(daysToKeep);

        // Find files with our prefix with a modified date before our cutoff
        for(File file : backupFiles){
            if(file.getName().startsWith(backupPrefix) && file.getName().endsWith(".zip")
                    && file.lastModified() < cutoffDate.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()){
                filesToPurge.add(file);
            }
        }

        // Delete files
        for(File file : filesToPurge){
            System.out.println("BackupUtil: Deleting - " + file.getName());
            file.delete();
        }
    }

}

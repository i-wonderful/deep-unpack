package org.byby.unzipsome.backup;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;

// Недоработано. Разархивировщик вложенных архиовов.

public class DeepUnpacker {

    private static final Integer MAX_ITERATE = 4100;

    public void go(String destDitPath, String fileZipPath, Integer numIterate, String baseDir) throws IOException {

        if (destDitPath == null || fileZipPath == null || numIterate > MAX_ITERATE) {
            System.out.println(String.format("Happy end, iterate %d", numIterate));
            return;
        }

        if (numIterate % 100 == 0) {
            destDitPath = baseDir + File.separator + numIterate + File.separator;
        }

        numIterate++;
        File destDir = new File(destDitPath);

        ISWrapper zis = new ISWrapper(fileZipPath);
        EntryWrapper zipEntry = zis.getNextEntry();

        String directoryName = null;
        String newDestDirPath = null;
        String newFileZipPath = null;

        while (zipEntry != null) {

            File newFile = newFile(destDir, zipEntry);

            if (zipEntry.isDirectory()) {
                if (!newFile.isDirectory() && !newFile.mkdirs()) {
                    throw new IOException("Failed to create directory " + newFile);
                }

                directoryName = zipEntry.getName();
            } else {
                // fix for Windows-created archives
                File parent = newFile.getParentFile();
                if (!parent.isDirectory() && !parent.mkdirs()) {
                    throw new IOException("Failed to create directory " + parent);
                }
                // write file content
                writeFileContent(newFile, zis);

                if (newFile.getName().endsWith(".zip") || newFile.getName().endsWith(".tar")) {
                    newFileZipPath = newFile.getPath();

//                    if (!newFile.exists()) {

//                    }
                } else {
                    if (!newFile.getName().equals("empty.txt")) {
                        System.out.println("Wow!!!!" + zipEntry.toString());
                        System.out.println(Files.readString(newFile.toPath()));
                        System.out.println(String.format("Iterate %d, file %s, %s", numIterate, newFile.getName(), zipEntry.toString()));
                        return;
                    }
                }

            }
            zipEntry = zis.getNextEntry();
        }

        zis.close();

        newDestDirPath = destDitPath + directoryName;

        go(newDestDirPath, newFileZipPath, numIterate, baseDir);
    }

    public File newFile(File destinationDir, EntryWrapper zipEntry) throws IOException {
        File destFile = new File(destinationDir, zipEntry.getName());

        String destDirPath = destinationDir.getCanonicalPath();
        String destFilePath = destFile.getCanonicalPath();

        if (!destFilePath.startsWith(destDirPath + File.separator)) {
            throw new IOException("Entry is outside of the target dir: " + zipEntry.getName());
        }

        return destFile;
    }

    // write file content
    private void writeFileContent(File newFile, ISWrapper zis) throws IOException {
        byte[] buffer = new byte[1024];

        FileOutputStream fos = new FileOutputStream(newFile);
        int len;
        while ((len = zis.read(buffer)) > 0) {
            fos.write(buffer, 0, len);
        }
        fos.close();
    }

}

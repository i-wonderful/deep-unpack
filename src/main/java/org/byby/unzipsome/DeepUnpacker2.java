package org.byby.unzipsome;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;

import java.io.*;

/**
 * Поиск в недрах архивов файла txt с ответом. Без разархивирования на диск.
 */
public class DeepUnpacker2 {
    private static final Integer MAX_ITERATE = 4100;

    public void go(String fileZipPath) throws IOException {
        oneUnpack(new FileInputStream(fileZipPath), isZip(fileZipPath), 0);
    }

    private void oneUnpack(InputStream archiveFileIS, Boolean isZip, Integer iterate) throws IOException {
        if (archiveFileIS == null || isZip == null || iterate > MAX_ITERATE) {
            System.out.println("Happy End");
            return;
        }

        ArchiveInputStream ais = isZip ? new ZipArchiveInputStream(archiveFileIS) : new TarArchiveInputStream(archiveFileIS);
        ArchiveEntry entry = ais.getNextEntry();

        System.out.println(iterate++);

        InputStream isInside = null;
        Boolean isZipArchive = null;
        while (entry != null) {
            if (entry.isDirectory()) {
                entry = ais.getNextEntry();
                continue;
            }

            if (entry.getName().endsWith(".txt")) {
                if (!entry.getName().endsWith("empty.txt")) {
                    System.out.println("Wow !!!!");
                    System.out.println(new String(ais.readAllBytes()));
                    return;
                } else {
                    entry = ais.getNextEntry();
                    continue;
                }
            }

            isZipArchive = isZip(entry.getName());
            if (isZipArchive != null) { // it is archive file txt
                isInside = new ByteArrayInputStream(ais.readAllBytes());
            }
            entry = ais.getNextEntry();
        }

        archiveFileIS.close();
        ais.close();
        oneUnpack(isInside, isZipArchive, iterate);
    }

    private Boolean isZip(String filePath) {
        if (filePath.endsWith(".zip")) {
            return true;
        } else if (filePath.endsWith(".tar")) {
            return false;
        }
        return null;
    }

}

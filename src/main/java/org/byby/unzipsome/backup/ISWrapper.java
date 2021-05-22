package org.byby.unzipsome.backup;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipEntry;


public class ISWrapper {
    ZipInputStream zip;
    ArchiveInputStream tar;

    public ISWrapper(String fileZipPath) throws IOException {
        if (fileZipPath.endsWith(".zip")) {
            zip = new ZipInputStream(new FileInputStream(fileZipPath));
            //zip = new ZipArchiveInputStream(new FileInputStream(fileZipPath));
        } else if (fileZipPath.endsWith(".tar")) {
            tar = new TarArchiveInputStream(new FileInputStream(fileZipPath));
        } else {
            throw new IOException("Unknown archive");
        }
    }

    public EntryWrapper getNextEntry() throws IOException {
        if (zip != null) {
            ZipEntry next = zip.getNextEntry();
            return next != null ? new EntryWrapper(next) : null;
        } else if (tar != null) {
            ArchiveEntry next = tar.getNextEntry();
            return next != null ? new EntryWrapper(next) : null;
        }
        return null;
    }



    public void close() throws IOException {
        if (zip != null) {
//            zip.closeEntry();
            zip.close();
        } else {
            tar.close();
        }
    }

    public int read(byte[] buffer) throws IOException {
        if (zip != null) {
            return zip.read(buffer);
        } else if (tar != null) {
            return tar.read(buffer);
        }
        return 0;
    }
}

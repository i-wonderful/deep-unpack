package org.byby.unzipsome.backup;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;

import java.util.zip.ZipEntry;

public class EntryWrapper {

    ZipEntry zipEntry;
    ArchiveEntry tarArchiveEntry;

    public EntryWrapper(ZipEntry zipEntry) {
        this.zipEntry = zipEntry;
    }

    public EntryWrapper(ArchiveEntry tarArchiveEntry) {
        this.tarArchiveEntry = tarArchiveEntry;
    }

    public boolean isDirectory() {
        if (zipEntry != null)
            return zipEntry.isDirectory();
        else
            return tarArchiveEntry.isDirectory();
    }

    public String getName() {
        if (zipEntry != null)
            return zipEntry.getName();
        else if (tarArchiveEntry != null) {
            return tarArchiveEntry.getName();
        }

        return null;
    }

    public long getSize() {
        if (zipEntry != null) {
            zipEntry.getSize();
        } else if (tarArchiveEntry != null) {
            return tarArchiveEntry.getSize();
        }
        return -1;
    }

    public String toString() {
        if (zipEntry != null) {
            return "Zip " + zipEntry.getName() + " " + zipEntry.getCompressedSize() + " " + zipEntry.getSize();
        } else if (tarArchiveEntry != null) {
            return "Tar " + tarArchiveEntry.getName() + " " + tarArchiveEntry.getSize();
        }

        return "";
    }
}

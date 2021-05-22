package org.byby.unzipsome;

import java.io.IOException;

public class Application {


    public static void main(String[] args) throws IOException {
        String fileZipPath = "src/main/resources/447509d6ee8b4cbaa96e3153ddddd7ba.zip";
        String destDirPath = "target/unzipTest/";

        new DeepUnpacker2().go(fileZipPath);

    }


//    private static boolean isArchive(File f) {
//        int fileSignature = 0;
//        try (RandomAccessFile raf = new RandomAccessFile(f, "r")) {
//            fileSignature = raf.readInt();
//        } catch (IOException e) {
//            // handle if you like
//        }
//        return fileSignature == 0x504B0304 || fileSignature == 0x504B0506 || fileSignature == 0x504B0708;
//    }
}

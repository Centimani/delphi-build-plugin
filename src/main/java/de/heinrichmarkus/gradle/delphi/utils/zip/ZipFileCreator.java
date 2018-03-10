package de.heinrichmarkus.gradle.delphi.utils.zip;

import de.heinrichmarkus.gradle.delphi.utils.exceptions.CreateZipException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZipFileCreator {

    private ZipFileCreator() {
        // Hide Constructor
    }

    public static void create(List<ZipFileMapping> mappings, String destFile) {
        create(mappings, new File(destFile));
    }

    public static void create(List<ZipFileMapping> mappings, File destFile) {
        try {
            FileOutputStream fos = new FileOutputStream(destFile);
            try {
                ZipOutputStream zipOutputStream = new ZipOutputStream(fos);
                for (ZipFileMapping m : mappings) {
                    addToZip(m, zipOutputStream);
                }
                zipOutputStream.close();
            } finally {
                fos.close();
            }
        } catch (IOException e) {
            throw new CreateZipException(e);
        }
    }

    private static void addToZip(ZipFileMapping mapping, ZipOutputStream zipOutputStream) throws IOException {
        FileInputStream fis = new FileInputStream(mapping.getSourceFile());
        try {
            ZipEntry zipEntry = new ZipEntry(mapping.getDestFileName());
            zipOutputStream.putNextEntry(zipEntry);

            byte[] bytes = new byte[1024];
            int length;
            while ((length = fis.read(bytes)) >= 0) {
                zipOutputStream.write(bytes, 0, length);
            }

            zipOutputStream.closeEntry();
        } finally {
            fis.close();
        }
    }
}
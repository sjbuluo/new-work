package com.sun.health.newwork.interview.java.network;

import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class SimpleZipOutputStreamTest {

    @Test
    public void test1() throws IOException {
        String testOutputDirPath = "D:/WorkSpace/testOutputDir/";
        File testOutputDir = new File(testOutputDirPath);
        if (!testOutputDir.exists()) {
            testOutputDir.mkdirs();
        }
        File testZip = new File(testOutputDirPath + "test.zip");
        if (!testZip.exists()) {
            testZip.createNewFile();
        }
        String[] txtFilePaths = new String[]{"1.txt", "2.txt", "3.txt"};
        byte[] data = new byte[1024];
        try (ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(testZip)); FileInputStream txt1 = new FileInputStream(testOutputDirPath + txtFilePaths[0]); FileInputStream txt2 = new FileInputStream(testOutputDirPath + txtFilePaths[1]); FileInputStream txt3 = new FileInputStream(testOutputDirPath + txtFilePaths[2]);){
            zos.putNextEntry(new ZipEntry(txtFilePaths[0]));
            while(txt1.read(data) != -1) {
                zos.write(data);
                zos.flush();
            }
            zos.putNextEntry(new ZipEntry(txtFilePaths[1]));
            while(txt2.read(data) != -1) {
                zos.write(data);
                zos.flush();
            }
            zos.putNextEntry(new ZipEntry(txtFilePaths[2]));
            while(txt3.read(data) != -1) {
                zos.write(data);
                zos.flush();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}

package com.sun.health.newwork.ms.file;

import org.junit.Test;

import java.io.*;
import java.nio.channels.FileChannel;
import java.nio.file.Files;

/**
 * Created by 华硕 on 2019-05-05.
 */
public class SimpleFileTest
{
    @Test
    public void test1() throws IOException {
        File file = new File("");
        Files.copy(file.toPath(), file.toPath());
    }

    @Test
    public void test2() throws IOException {
        File file = new File("");
        FileChannel inputChannel = new FileInputStream("").getChannel();
        FileChannel outputChannel = new FileOutputStream("").getChannel();
        outputChannel.transferFrom(inputChannel, 0, inputChannel.size());

    }
}

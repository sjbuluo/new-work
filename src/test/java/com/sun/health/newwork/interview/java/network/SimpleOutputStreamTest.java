package com.sun.health.newwork.interview.java.network;

import org.apache.commons.lang3.builder.ToStringExclude;
import org.junit.Test;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class SimpleOutputStreamTest {

    @Test
    public  void test1(){
        generateCharacters(System.out);
    }

    public void generateCharacters(OutputStream out) {
        int firstPrintableCharacter = 33;
        int numberOfPrintableCharacters = 94;
        int numberOfCharactersPerLine = 72;
        int start = firstPrintableCharacter;
        byte[] line = new byte[numberOfCharactersPerLine + 2];
        int n = 0;
        try {
            while (n++ < 10) {
                for (int i = start; i < start + numberOfCharactersPerLine; i++) {
                    line[i - start] = (byte) ((i - firstPrintableCharacter) % numberOfPrintableCharacters + firstPrintableCharacter);
                }
                line[72] = (byte) '\r';
                line[73] = (byte) '\n';
                out.write(line);
                start = ((start + 1) - firstPrintableCharacter) % numberOfPrintableCharacters + firstPrintableCharacter;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testAutoClosable() {
        try(OutputStream outputStream = new FileOutputStream(""); OutputStream out2 = new FileOutputStream("")) {
            System.out.println("成功");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

package com.sun.health.newwork.interview.java.network.thread;

import javax.xml.bind.DatatypeConverter;
import java.io.FileInputStream;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by 华硕 on 2019-05-20.
 */
public class NoSynchronziedRunnable implements Runnable {

    private String filename;

    public NoSynchronziedRunnable(String filename) {
        this.filename = filename;
    }

    @Override
    public void run() {
        try {
            MessageDigest sha = MessageDigest.getInstance("SHA-256");
            try (FileInputStream in = new FileInputStream(filename); DigestInputStream din = new DigestInputStream(in, sha)) {
                System.out.print(filename);
                Thread.yield();
                System.out.print(":");
                Thread.yield();
                while(din.read() != -1)
                    ;
                byte[] digest = sha.digest();
                System.out.print(DatatypeConverter.printHexBinary(digest));
                Thread.yield();
                System.out.println();
            } catch (Exception e) {

            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }
}

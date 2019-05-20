package com.sun.health.newwork.interview.java.network.thread;

import javax.xml.bind.DatatypeConverter;
import java.io.FileInputStream;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by 华硕 on 2019-05-20.
 */
public class DigestRunnable implements Runnable {

    private String filename;

    public DigestRunnable(String filename) {
        this.filename = filename;
    }

    @Override
    public void run() {
        try {
            MessageDigest sha = MessageDigest.getInstance("SHA-256");
            try (FileInputStream in = new FileInputStream(filename); DigestInputStream din = new DigestInputStream(in, sha)) {
                while(din.read() != -1) {

                }
                byte[] digest = sha.digest();
                StringBuilder result = new StringBuilder(filename);
                result.append(":");
                result.append(DatatypeConverter.printHexBinary(digest));
                System.out.println(result);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }
}

package com.sun.health.newwork.interview.java.network.thread;

import java.io.FileInputStream;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by 华硕 on 2019-05-20.
 */
public class CallbackDigestRunnable implements Runnable {

    private String filename;

    private DigestResultPrinter printer;

    public CallbackDigestRunnable(String filename, DigestResultPrinter printer) {
        this.filename = filename;
        this.printer = printer;
    }

    @Override
    public void run() {
        try {
            MessageDigest sha = MessageDigest.getInstance("SHA-256");
            try (FileInputStream in = new FileInputStream(filename); DigestInputStream din = new DigestInputStream(in, sha)) {
                while (din.read() != -1) {

                }
                byte[] digest = sha.digest();
                printer.printDigestResult(filename, digest);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }
}

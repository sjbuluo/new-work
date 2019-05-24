package com.sun.health.newwork.interview.java.network.url_connection;

import org.junit.Test;

import java.io.*;
import java.net.Socket;
import java.util.Date;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by 华硕 on 2019-05-24.
 */
public class Sender {

    public static void main(String[] args) {
        String name = "P" + new Random().nextInt(1000);
        try (Socket socket = new Socket("localhost", 6013);) {
            Scanner scanner = new Scanner(System.in);
            String nextLine = null;
            ExecutorService executorService = Executors.newSingleThreadExecutor();
            executorService.submit(() -> {
                String[] content = new String[10];
                int index = 0;
                try (InputStream inputStream = socket.getInputStream(); InputStreamReader inputStreamReader = new InputStreamReader(inputStream); BufferedReader bufferedReader = new BufferedReader(inputStreamReader)) {
                    while(true) {
//                        String line;
//                        while ((line = bufferedReader.readLine()) != null) {
//                            content[index++] = line;
//                        }
//                        System.out.println(content[0] + "(" + content[1] + "):");
//                        for (int i = 2; i < index; i++) {
//                            System.out.println(content[2]);
//                        }
//                        for (int i = 0; i < content.length; i++) {
//                            content[i] = null;
//                        }
//                        index = 0;
                        if (inputStream.read() != -1) {
                            int c;
                            StringBuilder sb = new StringBuilder('\0');
                            while ((c = inputStream.read()) != '\0') {
                                sb.append((char) c);
                            }
                            System.out.println(sb.toString());
                        }
                        TimeUnit.MILLISECONDS.sleep(500);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
            try (OutputStream outputStream = socket.getOutputStream(); OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream); BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter)) {
                while (!"exit".equals(nextLine)) {
                    nextLine = scanner.nextLine();
                    nextLine = '\0' + name + "\n" + new Date() + "\n" + nextLine + '\0';
                    bufferedWriter.write(nextLine);
                    bufferedWriter.flush();
                }
            }
            socket.shutdownInput();
            socket.shutdownOutput();
            executorService.shutdownNow();
            System.out.println("退出");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test1() {
        System.out.println((char) 104);
    }

}

package com.sun.health.newwork.base.controller;

import com.sun.health.newwork.activemq.Producer;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Created by 华硕 on 2019-05-07.
 */
@RestController
@RequestMapping("/base")
public class BaseController {

    @Autowired
    private AmqpTemplate amqpTemplate;

    @RequestMapping("/rabbitmq/send/hello/{msg}")
    public String sendToHelloQueue(@PathVariable("msg") String msg) {
        amqpTemplate.convertAndSend("hello", msg);
        return "将 [ " + msg +" ] 发送到RabbitMQ hello Queue中";
    }

    @Autowired
    private Producer producer;

    @RequestMapping("/activemq/send/{destination}")
    public String activeMQSend(@PathVariable("destination") String destination) {
        switch (destination) {
            case "queue":
                producer.sendToQueue();
                break;
            case "topic":
                producer.sendToTopic();
                break;
            default:
                break;
        }
        return "ok";
    }

    @RequestMapping("/zip")
    public void getZipStream(HttpServletResponse response) {
//        response.addHeader("Content-length",  String.valueOf(sumLength));
        response.setHeader("Content-Disposition", "attachment; filename=test.zip");
        response.setContentType("application/zip; charset=UTF-8"); // application/octet-stream
        String testZipOutputDirPath  = "D:/WorkSpace/testOutputDir/";
        String[] txtFilePaths = new String[] {"1.txt", "2.txt", "3.txt"};
        FileInputStream[] fileInputStreams = new FileInputStream[txtFilePaths.length];
        ZipOutputStream zipOutputStream = null;
        byte[] data = new byte[1024];
        int length = 0;
        int sumLength = 0;
        try {
            zipOutputStream = new ZipOutputStream(response.getOutputStream());
            for (int i = 0; i < txtFilePaths.length; i++) {
                fileInputStreams[i] = new FileInputStream(testZipOutputDirPath + txtFilePaths[i]);
                zipOutputStream.putNextEntry(new ZipEntry(txtFilePaths[i]));
                while((length = fileInputStreams[i].read(data)) != -1) {
                    zipOutputStream.write(data, 0, length);
                    zipOutputStream.flush();
                    sumLength += length;
                }
            }
            zipOutputStream.finish();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if(zipOutputStream != null) {
                try {
                    zipOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            for (FileInputStream fileInputStream : fileInputStreams) {
                if(fileInputStream != null) {
                    try {
                        fileInputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

}

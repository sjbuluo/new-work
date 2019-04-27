package com.sun.health.newwork.kafka;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.concurrent.FailureCallback;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.SuccessCallback;

@SpringBootTest
@RunWith(SpringRunner.class)
public class KafkaProducerTest {

    @Autowired
    private KafkaTemplate kafkaTemplate;

    @Test
    public void test1() {
        ListenableFuture future = kafkaTemplate.send("b", "从Spring Boot发送的数据");
        future.addCallback(new SuccessCallback() {
            @Override
            public void onSuccess(Object o) {
                System.out.println("成功发送数据回执: " + o);
            }
        }, new FailureCallback() {
            @Override
            public void onFailure(Throwable throwable) {
                throwable.printStackTrace();
            }
        });
    }

}

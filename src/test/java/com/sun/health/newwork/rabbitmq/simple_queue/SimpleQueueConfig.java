package com.sun.health.newwork.rabbitmq.simple_queue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public interface SimpleQueueConfig {

    String SIMPLE_QUEUE_NAME =  "test_simple_queue";

    String SIMPLE_QUEUE_EXCHANGE_NAME = "simple_queue_exchange";

    String SIMPLE_QUEUE_BINDING_KEY = "simple_queue";

    String SIMPLE_QUEUE_ROUTING_KEY = "simple_queue";

}

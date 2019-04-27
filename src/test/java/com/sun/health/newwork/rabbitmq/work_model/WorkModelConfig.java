package com.sun.health.newwork.rabbitmq.work_model;

public interface WorkModelConfig {
    String WORK_MODEL_QUEUE = "work_model_queue";
    String WORK_MODEL_EXCHANGE = "work_model_exchange";
    String WORK_MODEL_ROUTING_KEY = "work.model";
    String WORK_MODEL_BINDING_KEY = "work.*";
}

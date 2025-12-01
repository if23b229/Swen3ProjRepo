package com.swen3.paperless.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    @Value("${app.mq.exchange}")
    String exchangeName;

    @Value("${app.mq.ocrQueue}")
    String ocrQueueName;

    @Value("${app.mq.resultQueue}")
    String resultQueueName;

    @Value("${app.mq.genAiQueue}")
    String genAiQueueName;

    @Value("${app.mq.genAiResultQueue}")
    String genAiResultQueueName;

    @Value("${app.mq.ocrKey}")
    String ocrKey;

    @Value("${app.mq.resultKey}")
    String resultKey;

    @Value("${app.mq.genAiKey}")
    String genAiKey;

    @Value("${app.mq.genAiResultKey}")
    String genAiResultKey;

    @Bean
    TopicExchange exchange() {
        return ExchangeBuilder.topicExchange(exchangeName).durable(true).build();
    }

    @Bean
    Queue ocrQueue() {
        return QueueBuilder.durable(ocrQueueName).build();
    }

    @Bean
    Queue resultQueue() {
        return QueueBuilder.durable(resultQueueName).build();
    }

    @Bean
    Queue genAiQueue() {
        return QueueBuilder.durable(genAiQueueName).build();
    }

    @Bean
    Queue genAiResultQueue() {
        return QueueBuilder.durable(genAiResultQueueName).build();
    }

    @Bean
    Binding bindOcr(Queue ocrQueue, TopicExchange exchange) {
        return BindingBuilder.bind(ocrQueue).to(exchange).with(ocrKey);
    }

    @Bean
    Binding bindOcrResult(Queue resultQueue, TopicExchange exchange) {
        return BindingBuilder.bind(resultQueue).to(exchange).with(resultKey);
    }

    @Bean
    Binding bindGenAi(Queue genAiQueue, TopicExchange exchange) {
        return BindingBuilder.bind(genAiQueue).to(exchange).with(genAiKey);
    }

    @Bean
    Binding bindGenAiResult(Queue genAiResultQueue, TopicExchange exchange) {
        return BindingBuilder.bind(genAiResultQueue).to(exchange).with(genAiResultKey);
    }

    @Bean
    Jackson2JsonMessageConverter jacksonConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    RabbitTemplate rabbitTemplate(ConnectionFactory cf, Jackson2JsonMessageConverter conv) {
        RabbitTemplate tpl = new RabbitTemplate(cf);
        tpl.setMessageConverter(conv);
        return tpl;
    }
}

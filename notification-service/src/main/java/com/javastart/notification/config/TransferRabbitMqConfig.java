package com.javastart.notification.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TransferRabbitMqConfig {

    public static final String QUEUE = "transfer.notify";

    private static final String TOPIC_EXCHANGE = "transfer.notify.exchanger";

    private static final String ROUTING_KEY = "transfer.notify.routingKey";

    @Autowired
    private AmqpAdmin amqpAdmin;

    @Bean
    public Queue queue(){
        return new Queue(QUEUE);
    }

    @Bean
    public TopicExchange exchanger(){
        return new TopicExchange(TOPIC_EXCHANGE);
    }

    @Bean
    public Binding binding(){
        return BindingBuilder.bind(queue())
                .to(exchanger())
                .with(ROUTING_KEY);
    }
}

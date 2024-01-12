package com.javastart.notification.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DepositRabbitMqConfig {

    public static final String QUEUE = "deposit.notify";

    private static final String TOPIC_EXCHANGE = "deposit.notify.exchanger";

    private static final String ROUTING_KEY = "deposit.notify.routingKey";

    @Autowired
    private AmqpAdmin amqpAdmin;

    @Bean
    public Queue depositqueue(){
        return new Queue(QUEUE);
    }

    @Bean
    public TopicExchange depositExchange(){
        return new TopicExchange(TOPIC_EXCHANGE);
    }

    @Bean
    public Binding depositBinding(){
        return BindingBuilder.bind(depositqueue())
                .to(depositExchange())
                .with(ROUTING_KEY);
    }




}

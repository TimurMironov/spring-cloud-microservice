package com.javastart.notification.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TransactionRabbitMqConfig {

    public static final String QUEUE = "tansarction.notify";

    private static final String TOPIC_EXCHANGE = "transaction.notify.exchanger";

    private static final String ROUTING_KEY = "transaction.notify.routingKey";

    @Autowired
    private AmqpAdmin amqpAdmin;

    @Bean
    public Queue transactionQueue(){
        return new Queue(QUEUE);
    }

    @Bean
    public TopicExchange topicExchange(){
        return new TopicExchange(TOPIC_EXCHANGE);
    }

    @Bean
    public Binding binding(){
        return BindingBuilder.bind(transactionQueue())
                .to(topicExchange())
                .with(ROUTING_KEY);
    }


}

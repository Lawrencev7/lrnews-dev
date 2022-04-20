package com.lrnews.api.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for RabbitMQ
 */
@Configuration
public class RabbitMQConfig {
    // Exchange name
    public static final String EXCHANGE_ARTICLE = "exchange_article";

    // Queue name
    public static final String QUEUE_ARTICLE_FS = "article_fs_queue";

    /**
     * Rabbit MQ routing key rules:
     * 1.Every * is a placeholder, and it can only be replaced by one word.
     *      test.* can match test.a, but not test.a.b
     *
     * 2.Every # can be replaced by any number of words.
     *      test.# can match test.a.b or test.a.b.c.d
     *
     * 3.A hardcode routing key can only match the same key.
     */
    public static final String BINDING_ROUTING_KEY = "article.*";

    @Bean(EXCHANGE_ARTICLE)
    public Exchange exchange(){
        return ExchangeBuilder
                .topicExchange(EXCHANGE_ARTICLE)
                .durable(true)
                .build();
    }

    @Bean(QUEUE_ARTICLE_FS)
    public Queue queue(){
        return new Queue(QUEUE_ARTICLE_FS, true);
    }

    @Bean
    public Binding binding(@Qualifier(QUEUE_ARTICLE_FS) Queue queue,
                           @Qualifier(EXCHANGE_ARTICLE) Exchange exchange){
        return BindingBuilder
                .bind(queue)
                .to(exchange)
                .with(BINDING_ROUTING_KEY)
                .noargs(); // do bind
    }
}

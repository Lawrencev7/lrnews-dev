package com.lrnews.api.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for RabbitMQ
 */
@Configuration
public class RabbitLazyMQConfig {
    // Exchange name
    public static final String EXCHANGE_ARTICLE_LAZY = "exchange_article_lazy";

    // Queue name
    public static final String QUEUE_ARTICLE_FS_LAZY = "article_fs_queue_lazy";

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
    public static final String BINDING_ROUTING_KEY = "article.lazy.#";

    @Bean(EXCHANGE_ARTICLE_LAZY)
    public Exchange exchange(){
        return ExchangeBuilder
                .topicExchange(EXCHANGE_ARTICLE_LAZY)
                .delayed()
                .durable(true)
                .build();
    }

    @Bean(QUEUE_ARTICLE_FS_LAZY)
    public Queue queue(){
        return new Queue(QUEUE_ARTICLE_FS_LAZY, true);
    }

    @Bean
    public Binding bindingDelayMQ(@Qualifier(QUEUE_ARTICLE_FS_LAZY) Queue queue,
                           @Qualifier(EXCHANGE_ARTICLE_LAZY) Exchange exchange){
        return BindingBuilder
                .bind(queue)
                .to(exchange)
                .with(BINDING_ROUTING_KEY)
                .noargs(); // do bind
    }
}

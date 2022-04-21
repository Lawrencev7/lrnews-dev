package com.lrnews.article.fs.consumer;

import com.lrnews.api.config.RabbitLazyMQConfig;
import com.lrnews.api.config.RabbitMQConfig;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class RabbitMQConsumer {
    @RabbitListener(queues = {RabbitMQConfig.QUEUE_ARTICLE_FS})
    public void watchQueue(String payload, Message message) {
        System.out.println("watchQueue" + payload);
        System.out.println("watchQueue" + message);
    }
}

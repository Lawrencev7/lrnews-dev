package com.lrnews.article.task;

import com.lrnews.api.config.RabbitLazyMQConfig;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public interface RabbitLazyMQConsumer {
    @RabbitListener(queues = {RabbitLazyMQConfig.QUEUE_ARTICLE_FS_LAZY})
    void watchQueueDelay(String payload, Message message);
}

package com.lrnews.article.stream.consumer;

import com.lrnews.article.stream.SteamChannel;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.stereotype.Component;

@Component
@EnableBinding(SteamChannel.class) // 消费端同样绑定到通道
public class StreamConsumer {

    // 监听channel中定义的input，并且实现消息的消费
//    @StreamListener(ArticleSteamChannel.INPUT)
//    public void watchChannel(Object msg) {
//        System.out.println(msg.toString());
//    }

    @StreamListener(SteamChannel.INPUT)
    public void consume(String msg) {
        System.out.println("StreamConsumer: " + msg + " was consumed");
    }
}

package com.lrnews.article.stream.impl;

import com.lrnews.article.stream.SteamChannel;
import com.lrnews.article.stream.StreamService;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

@Component
@EnableBinding(SteamChannel.class) // 开启绑定器，绑定通道Channel
public class StreamServiceImpl implements StreamService {

    private final SteamChannel ASChannel;

    public StreamServiceImpl(SteamChannel ASChannel) {
        this.ASChannel = ASChannel;
    }

    @Override
    public void sendMsg(Object obj) {
        ASChannel.output().send(MessageBuilder.withPayload(obj).build());
    }

    @Override
    public void send(String msg) {
        ASChannel.output().send(
                MessageBuilder.withPayload("StreamService: " + msg + " was sent").build());
    }
}

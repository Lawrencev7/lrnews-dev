package com.lrnews.article.stream;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;

public interface SteamChannel {
    String INPUT = "lrnewsInput";
    String OUTPUT = "lrnewsOutput";

    @Output(SteamChannel.OUTPUT)
    MessageChannel output();

    @Input(SteamChannel.INPUT)
    SubscribableChannel input();
}

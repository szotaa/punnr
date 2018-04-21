package pl.szotaa.util;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.ChannelInterceptorAdapter;

public class TestChannelInterceptor extends ChannelInterceptorAdapter {

    @Override
    public Message<?> postReceive(Message<?> message, MessageChannel channel) {
        return message;
    }
}

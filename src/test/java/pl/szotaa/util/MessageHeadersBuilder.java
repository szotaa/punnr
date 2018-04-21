package pl.szotaa.util;

import lombok.Getter;
import lombok.Setter;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;

import java.util.HashMap;

public class MessageHeadersBuilder {

    @Getter
    @Setter
    private static String principalName = "exampleName";

    public static MessageHeaders sendTo(String destination){
        return buildStompHeaderAccessor(StompCommand.SEND, destination).getMessageHeaders();
    }

    public static MessageHeaders subscribeTo(String destination){
        return buildStompHeaderAccessor(StompCommand.SUBSCRIBE, destination).getMessageHeaders();
    }

    private static StompHeaderAccessor buildStompHeaderAccessor(StompCommand stompCommand, String destination){
        StompHeaderAccessor headers = StompHeaderAccessor.create(stompCommand);
        headers.setSubscriptionId("0");
        headers.setDestination(destination);
        headers.setSessionId("0");
        headers.setSessionAttributes(new HashMap<>());
        headers.setUser(new TestPrincipal(principalName));
        return headers;
    }
}

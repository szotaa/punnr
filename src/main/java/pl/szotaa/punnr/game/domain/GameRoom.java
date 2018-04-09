package pl.szotaa.punnr.game.domain;

import lombok.EqualsAndHashCode;
import pl.szotaa.punnr.game.message.ChatMessage;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

@EqualsAndHashCode
public class GameRoom {

    private final Queue<ChatMessage> chat = new ConcurrentLinkedQueue<>();

    public void addChatMessage(ChatMessage message){
        chat.add(message);
    }
}

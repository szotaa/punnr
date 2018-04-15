package pl.szotaa.punnr.game.messenger;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
@RequiredArgsConstructor
public class Messenger {

    private final SimpMessagingTemplate messagingTemplate;

    public void sendToAll(String gameId, Object message){
        messagingTemplate.convertAndSend("/user/queue/" + gameId, message);
    }

    public void sendAllToUser(String gameId, String username, Collection<?> messages){
        messages
                .forEach(message -> {
                    try{
                        messagingTemplate.convertAndSendToUser(username, "/queue/" + gameId, message);
                    } catch (Exception e){
                        throw new RuntimeException("Messenger.sendAllToUser(String, String, Collection<?>) sth went wrong");
                    }
                });
    }

    public void sendObjectToUser(String gameId, String username, Object object){
        messagingTemplate.convertAndSendToUser(username, "/queue/" + gameId, object);
    }
}

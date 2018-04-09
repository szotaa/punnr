package pl.szotaa.punnr.game.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import pl.szotaa.punnr.game.message.ChatMessage;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatService {

    private final GameRoomService gameRoomService;
    private final SimpMessagingTemplate messagingTemplate;

    public void processChatMessage(String gameId, ChatMessage message){
        gameRoomService.getById(gameId).addChatMessage(message);
        messagingTemplate.convertAndSend("/user/queue/" + gameId, message);
    }

    public void sendAllMessages(String gameId, String username){
        gameRoomService.getById(gameId)
                .getChat()
                .forEach(message -> {
                    try{
                        log.info("Sending message: " + message.getContent());
                        messagingTemplate.convertAndSendToUser(username, "/queue/" + gameId, message);
                    } catch (Exception e){
                        throw new RuntimeException("ChatService.sendAllMessages(String, Principal) sth went wrong");
                    }
                });
    }
}

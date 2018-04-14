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
        message.setMessageType(ChatMessage.MessageType.CHAT);
        sendToAll(gameId, message);
        checkIfWinning(gameId, message);
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

    private void checkIfWinning(String gameId, ChatMessage message){
        if (gameRoomService.getById(gameId).getCurrentDrawingTitle().compareToIgnoreCase(message.getContent()) == 0){
            log.info(message.getAuthor() + " WON!!!");
            gameRoomService.getById(gameId).clearDrawing();
            //add points
            sendToAll(gameId, new ChatMessage(ChatMessage.MessageType.SERVER_WON, message.getAuthor(), null));
        }
    }

    private void sendToAll(String gameId, ChatMessage message){
        gameRoomService.getById(gameId).addChatMessage(message);
        messagingTemplate.convertAndSend("/user/queue/" + gameId, message);
    }
}

package pl.szotaa.punnr.game.service;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import pl.szotaa.punnr.game.message.ChatMessage;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final GameRoomService gameRoomService;
    private final SimpMessagingTemplate messagingTemplate;

    public void processChatMessage(String gameId, ChatMessage message){
        gameRoomService.getById(gameId).addChatMessage(message);
        messagingTemplate.convertAndSend("/topic/" + gameId, message);
    }
}

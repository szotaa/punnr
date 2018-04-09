package pl.szotaa.punnr.game.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;
import pl.szotaa.punnr.game.message.ChatMessage;
import pl.szotaa.punnr.game.service.ChatService;

@Controller
@MessageMapping("/game")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    @MessageMapping("/{gameId}/chat")
    public void processChatMessage(@DestinationVariable String gameId, @Payload ChatMessage message){
        chatService.processChatMessage(gameId, message);
    }
}

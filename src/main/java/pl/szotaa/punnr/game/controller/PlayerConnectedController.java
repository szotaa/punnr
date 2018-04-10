package pl.szotaa.punnr.game.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;
import pl.szotaa.punnr.game.service.ChatService;
import pl.szotaa.punnr.game.service.DrawingService;

import java.security.Principal;

@Slf4j
@Controller
@MessageMapping("/user/queue")
@RequiredArgsConstructor
public class PlayerConnectedController {

    private final ChatService chatService;
    private final DrawingService drawingService;

    @SubscribeMapping("/{gameId}")
    public void onPlayerConnected(@DestinationVariable String gameId, Principal principal){
        String username = principal.getName();
        chatService.sendAllMessages(gameId, username);
        drawingService.sendAllLines(gameId, username);
    }
}

package pl.szotaa.punnr.game.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;
import pl.szotaa.punnr.game.service.ChatService;
import pl.szotaa.punnr.game.service.DrawingService;
import pl.szotaa.punnr.game.service.ScoreService;

import java.security.Principal;

@Controller
@MessageMapping("/user/queue")
@RequiredArgsConstructor
public class PlayerConnectedController {

    private final ChatService chatService;
    private final DrawingService drawingService;
    private final ScoreService scoreService;

    @SubscribeMapping("/{gameId}")
    public void onPlayerConnected(@DestinationVariable String gameId, Principal principal){
        String username = principal.getName();
        chatService.sendAllChatMessages(gameId, username);
        drawingService.sendAllLines(gameId, username);
        scoreService.sendScoreboard(gameId, username);
    }
}

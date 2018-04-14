package pl.szotaa.punnr.game.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;
import pl.szotaa.punnr.game.message.Line;
import pl.szotaa.punnr.game.service.DrawingService;

@Controller
@MessageMapping("/game")
@RequiredArgsConstructor
public class DrawingController {

    private final DrawingService drawingService;

    @MessageMapping("/{gameId}/draw")
    public void processLine(@DestinationVariable String gameId, @Payload Line line){
        drawingService.receiveLine(gameId, line);
    }
}

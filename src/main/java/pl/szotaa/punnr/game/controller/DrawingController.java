package pl.szotaa.punnr.game.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;
import pl.szotaa.punnr.game.message.Line;
import pl.szotaa.punnr.game.service.DrawingService;

@Slf4j
@Controller
@MessageMapping("/game")
@RequiredArgsConstructor
public class DrawingController {

    private final DrawingService drawingService;

    @MessageMapping("/{gameId}/draw")
    public void processLine(@DestinationVariable String gameId, @Payload Line line){
        log.info("recieved line: " + line.toString());
        drawingService.processLine(gameId, line);
    }
}

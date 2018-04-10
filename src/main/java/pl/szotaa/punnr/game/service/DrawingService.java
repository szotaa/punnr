package pl.szotaa.punnr.game.service;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import pl.szotaa.punnr.game.message.Line;

@Service
@RequiredArgsConstructor
public class DrawingService {

    private final GameRoomService gameRoomService;
    private final SimpMessagingTemplate messagingTemplate;

    public void processLine(String gameId, Line line){
        gameRoomService.getById(gameId).addLine(line);
        messagingTemplate.convertAndSend("/user/queue/" + gameId, line);
    }

    public void sendAllLines(String gameId, String username){
        gameRoomService.getById(gameId)
                .getDrawing()
                .parallelStream()
                .forEach(line -> {
                    try{
                        messagingTemplate.convertAndSendToUser(username, "/queue/" + gameId, line);
                    } catch (Exception e){
                        throw new RuntimeException("DrawingService.sendAllMessages(String, String) sth went wrong");
                    }
                });
    }
}

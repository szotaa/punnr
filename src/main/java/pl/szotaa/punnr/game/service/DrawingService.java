package pl.szotaa.punnr.game.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.szotaa.punnr.game.holder.GameRoomHolder;
import pl.szotaa.punnr.game.message.Line;
import pl.szotaa.punnr.game.messenger.Messenger;

@Service
@RequiredArgsConstructor
public class DrawingService {

    private final GameRoomHolder gameRoomHolder;
    private final Messenger messenger;

    public void receiveLine(String gameId, Line line){
        gameRoomHolder.getById(gameId).getDrawing().add(line);
        messenger.sendToAll(gameId, line);
    }

    public void sendAllLines(String gameId, String username){
        messenger.sendAllToUser(gameId, username, gameRoomHolder.getById(gameId).getDrawing());
    }
}

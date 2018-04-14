package pl.szotaa.punnr.game.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.szotaa.punnr.game.domain.GameRoom;
import pl.szotaa.punnr.game.holder.GameRoomHolder;
import pl.szotaa.punnr.game.message.Event;
import pl.szotaa.punnr.game.messenger.Messenger;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GameRoomService {

    private final GameRoomHolder gameRoomHolder;
    private final GameService gameService;
    private final Messenger messenger;

    public String create(){
        String gameId = generateGameId();
        gameRoomHolder.getGameRooms().put(gameId, new GameRoom());
        gameService.startNewRound(gameId);
        return gameId;
    }

    public void join(String gameId, String username){
        gameRoomHolder.getById(gameId).getPlayers().add(username);
        messenger.sendToAll(gameId, new Event(Event.EventType.PLAYER_JOINED, username, null));
    }

    private String generateGameId(){
        return UUID.randomUUID().toString();
    }
}

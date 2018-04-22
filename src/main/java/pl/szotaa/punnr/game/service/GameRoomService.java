package pl.szotaa.punnr.game.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.szotaa.punnr.game.domain.GameRoom;
import pl.szotaa.punnr.game.holder.GameRoomHolder;
import pl.szotaa.punnr.game.message.Event;
import pl.szotaa.punnr.game.messenger.Messenger;

import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GameRoomService {

    private static final long INITIAL_SCORE = 0L;

    private final GameRoomHolder gameRoomHolder;
    private final GameService gameService;
    private final Messenger messenger;

    public String create(){
        String gameId = generateGameId();
        gameRoomHolder.getGameRooms().put(gameId, new GameRoom());
        return gameId;
    }

    public void join(String gameId, String username){
        GameRoom gameRoom = gameRoomHolder.getById(gameId);
        if(!gameRoom.getPlayers().contains(username)){
            gameRoom.getPlayers().add(username);
            gameRoom.getScoreboard().put(username, INITIAL_SCORE);
            messenger.sendToAll(gameId, new Event(Event.EventType.PLAYER_JOINED, username, null));
        }

        if(gameRoom.getPlayers().size() == 1){
            gameService.startNewRound(gameId);
        }
    }

    public Map<String, GameRoom> getAllGames(){
        return gameRoomHolder.getGameRooms();
    }

    private String generateGameId(){
        return UUID.randomUUID().toString();
    }
}

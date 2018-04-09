package pl.szotaa.punnr.game.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.szotaa.punnr.game.domain.GameRoom;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
@RequiredArgsConstructor
public class GameRoomService {

    private final Map<String, GameRoom> gameRooms = new ConcurrentHashMap<>();

    public String create(){
        String gameId = generateGameId();
        gameRooms.put(gameId, new GameRoom());
        return gameId;
    }

    public GameRoom getById(String gameId){
        return gameRooms.get(gameId);
    }

    public void remove(String key){
        gameRooms.remove(key);
    }

    private String generateGameId(){
        return UUID.randomUUID().toString();
    }
}

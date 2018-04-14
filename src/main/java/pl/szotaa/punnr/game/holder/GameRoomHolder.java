package pl.szotaa.punnr.game.holder;

import lombok.Getter;
import org.springframework.stereotype.Component;
import pl.szotaa.punnr.game.domain.GameRoom;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Getter
@Component
public class GameRoomHolder {

    private final Map<String, GameRoom> gameRooms = new ConcurrentHashMap<>();

    public GameRoom getById(String gameId){
        return gameRooms.get(gameId);
    }
}

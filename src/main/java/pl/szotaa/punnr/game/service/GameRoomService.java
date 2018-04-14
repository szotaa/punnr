package pl.szotaa.punnr.game.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.szotaa.punnr.game.domain.GameRoom;
import pl.szotaa.punnr.game.holder.GameRoomHolder;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class GameRoomService {

    private final GameRoomHolder gameRoomHolder;

    public String create(){
        String gameId = generateGameId();
        gameRoomHolder.getGameRooms().put(gameId, new GameRoom());
        return gameId;
    }

    private String generateGameId(){
        return UUID.randomUUID().toString();
    }
}

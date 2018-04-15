package pl.szotaa.punnr.game.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.szotaa.punnr.game.holder.GameRoomHolder;
import pl.szotaa.punnr.game.messenger.Messenger;

@Service
@RequiredArgsConstructor
public class ScoreService {

    private final GameRoomHolder gameRoomHolder;
    private final Messenger messenger;

    public void sendScoreboard(String gameId, String username){
        messenger.sendObjectToUser(gameId, username, gameRoomHolder.getById(gameId).getScoreboard());
    }
}

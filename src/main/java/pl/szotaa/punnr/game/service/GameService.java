package pl.szotaa.punnr.game.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.szotaa.punnr.game.domain.GameRoom;
import pl.szotaa.punnr.game.holder.GameRoomHolder;
import pl.szotaa.punnr.game.message.ChatMessage;
import pl.szotaa.punnr.game.message.Event;
import pl.szotaa.punnr.game.messenger.Messenger;

import java.util.concurrent.ConcurrentLinkedQueue;

@Service
@RequiredArgsConstructor
public class GameService {

    private final GameRoomHolder gameRoomHolder;
    private final Messenger messenger;

    public void processPotentialAnswer(String gameId, ChatMessage answer){
        if(isAnswerCorrect(gameId, answer.getContent())){
            messenger.sendToAll(gameId, new Event(Event.EventType.ROUND_WON, answer.getAuthor(), null));
            //add points
            startNewRound(gameId);
        }
    }

    public void startNewRound(String gameId){
        GameRoom gameRoom = gameRoomHolder.getById(gameId);
        gameRoom.setDrawing(new ConcurrentLinkedQueue<>());
        gameRoom.setCurrentDrawingTitle("exampleTitle");
        messenger.sendToAll(gameId, new Event(Event.EventType.ROUND_STARTED, null, null));
    }

    private boolean isAnswerCorrect(String gameId, String answer){
        return gameRoomHolder.getById(gameId).getCurrentDrawingTitle().compareToIgnoreCase(answer) == 0;
    }
}

package pl.szotaa.punnr.game.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.szotaa.punnr.game.domain.GameRoom;
import pl.szotaa.punnr.game.holder.GameRoomHolder;
import pl.szotaa.punnr.game.message.ChatMessage;
import pl.szotaa.punnr.game.message.Event;
import pl.szotaa.punnr.game.messenger.Messenger;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ThreadLocalRandom;

@Slf4j
@Service
@RequiredArgsConstructor
public class GameService {

    private static final long WIN_REWARD = 10;

    private final GameRoomHolder gameRoomHolder;
    private final Messenger messenger;

    public void processPotentialAnswer(String gameId, ChatMessage answer){
        if(isAnswerCorrect(gameId, answer.getContent())){
            GameRoom gameRoom = gameRoomHolder.getById(gameId);
            messenger.sendToAll(gameId, new Event(Event.EventType.ROUND_WON, answer.getAuthor(), answer.getContent()));
            gameRoom.getScoreboard().merge(answer.getAuthor(), WIN_REWARD, Long::sum);
            gameRoom.getScoreboard().merge(gameRoom.getCurrentDrawer(), WIN_REWARD, Long::sum);
            startNewRound(gameId);
        }
    }

    public void startNewRound(String gameId){
        GameRoom gameRoom = gameRoomHolder.getById(gameId);
        gameRoom.setDrawing(new ConcurrentLinkedQueue<>());
        gameRoom.setCurrentDrawingTitle(getAnotherAnswer(gameRoom.getCurrentDrawingTitle()));
        gameRoom.setCurrentDrawer(getNextDrawer(gameId));
        messenger.sendToAll(gameId, new Event(Event.EventType.ROUND_STARTED, gameRoom.getCurrentDrawer(), null));
        messenger.sendObjectToUser(gameId, gameRoom.getCurrentDrawer(), new Event(Event.EventType.YOU_ARE_DRAWING, null, gameRoom.getCurrentDrawingTitle()));
    }

    private boolean isAnswerCorrect(String gameId, String answer){
        return gameRoomHolder.getById(gameId).getCurrentDrawingTitle().compareToIgnoreCase(answer) == 0;
    }

    private String getAnotherAnswer(String currentAnswer){ //TODO: clean this up
        ObjectMapper objectMapper = new ObjectMapper();
        String result = null;
        try {
            InputStream answerStream = this.getClass().getResourceAsStream("/answers.json");
            JsonNode node = objectMapper.readTree(answerStream).get("answers");
            List<String> answers = new ArrayList<>();
            node.forEach(answer -> answers.add(answer.asText()));
            if(currentAnswer != null){
                answers.remove(currentAnswer);
            }
            result = answers.get(ThreadLocalRandom.current().nextInt(0, answers.size()));

        } catch (Exception e){

        }
        return result;
    }

    private String getNextDrawer(String gameId){
        GameRoom gameRoom = gameRoomHolder.getById(gameId);
        Iterator<String> iterator = gameRoom.getIterator();
        if(gameRoom.getPlayers().size() == 1){
            return gameRoom.getPlayers().peek();
        }
        else if(iterator.hasNext()){
            return iterator.next();
        }
        else {
            gameRoom.setIterator(gameRoom.getPlayers().iterator());
            return gameRoom.getIterator().next();
        }
    }
}

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
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ThreadLocalRandom;

@Slf4j
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
        gameRoom.setCurrentDrawingTitle(getAnotherAnswer(gameRoom.getCurrentDrawingTitle()));
        log.info(gameRoom.getCurrentDrawingTitle());
        messenger.sendToAll(gameId, new Event(Event.EventType.ROUND_STARTED, null, null));
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
}

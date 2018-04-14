package pl.szotaa.punnr.game.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import pl.szotaa.punnr.game.message.ChatMessage;
import pl.szotaa.punnr.game.message.Line;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

@Getter
@EqualsAndHashCode
public class GameRoom {

    private final Queue<ChatMessage> chat = new ConcurrentLinkedQueue<>();
    private Queue<Line> drawing = new ConcurrentLinkedQueue<>();
    private String currentDrawingTitle = "elo";

    public void addChatMessage(ChatMessage message){
        chat.add(message);
    }

    public void clearDrawing(){
        drawing = new ConcurrentLinkedQueue<>();
    }

    public void addLine(Line line){
        drawing.add(line);
    }
}

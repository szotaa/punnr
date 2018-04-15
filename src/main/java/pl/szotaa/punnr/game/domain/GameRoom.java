package pl.szotaa.punnr.game.domain;

import lombok.Data;
import pl.szotaa.punnr.game.message.ChatMessage;
import pl.szotaa.punnr.game.message.Line;

import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

@Data
public class GameRoom {

    private Queue<String> players = new ConcurrentLinkedQueue<>();

    private Queue<ChatMessage> chat = new ConcurrentLinkedQueue<>();

    private Queue<Line> drawing = new ConcurrentLinkedQueue<>();

    private Map<String, Long> scoreboard = new ConcurrentHashMap<>();

    private String currentDrawingTitle;

    private String currentDrawer;
}

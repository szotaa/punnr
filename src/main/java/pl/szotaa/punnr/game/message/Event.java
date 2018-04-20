package pl.szotaa.punnr.game.message;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonRootName("event")
public class Event {

    private EventType eventType;
    private String author;
    private String message;

    public enum EventType{
        ROUND_WON, ROUND_STARTED, PLAYER_JOINED, PLAYER_LEFT, YOU_ARE_DRAWING, GAME_ENDED
    }
}

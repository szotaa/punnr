package pl.szotaa.punnr.game.message;

import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@JsonRootName("chatMessage")
public class ChatMessage {

    private MessageType messageType;
    private String author;
    private String content;

    public enum MessageType {
        CHAT, SERVER_WON
    }
}

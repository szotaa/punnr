package pl.szotaa.punnr.game.message;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonRootName("chatMessage")
public class ChatMessage {

    private String author;
    private String content;
}

package pl.szotaa.punnr.game.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.szotaa.punnr.game.holder.GameRoomHolder;
import pl.szotaa.punnr.game.message.ChatMessage;
import pl.szotaa.punnr.game.messenger.Messenger;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final GameRoomHolder gameRoomHolder;
    private final GameService gameService;
    private final Messenger messenger;

    public void receiveChatMessage(String gameId, ChatMessage message){
        gameRoomHolder.getById(gameId).getChat().add(message);
        messenger.sendToAll(gameId, message);
        gameService.processPotentialAnswer(gameId, message);
    }

    public void sendAllChatMessages(String gameId, String username){
        messenger.sendAllToUser(gameId, username, gameRoomHolder.getById(gameId).getChat());
    }
}

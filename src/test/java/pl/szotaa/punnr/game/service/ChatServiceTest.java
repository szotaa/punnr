package pl.szotaa.punnr.game.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringRunner;
import pl.szotaa.UnitTest;
import pl.szotaa.punnr.game.domain.GameRoom;
import pl.szotaa.punnr.game.holder.GameRoomHolder;
import pl.szotaa.punnr.game.message.ChatMessage;
import pl.szotaa.punnr.game.messenger.Messenger;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@Category(UnitTest.class)
public class ChatServiceTest {

    @InjectMocks
    private ChatService chatService;

    @Mock
    private GameRoomHolder gameRoomHolder;

    @Mock
    private GameService gameService;

    @Mock
    private Messenger messenger;

    private GameRoom testGameRoom;

    private ArgumentCaptor<ChatMessage> chatMessageCaptor = ArgumentCaptor.forClass(ChatMessage.class);

    private ArgumentCaptor<Collection<?>> collectionArgumentCaptor = ArgumentCaptor.forClass(Collection.class);

    @Before
    public void setUp(){
        testGameRoom = new GameRoom();
        testGameRoom.setCurrentDrawingTitle("correctAnswer");
        testGameRoom.setCurrentDrawer("currentDrawer");
        testGameRoom.getChat().addAll(Arrays.asList(
                new ChatMessage("author1", "content1"),
                new ChatMessage("author2", "content2")
        ));
    }

    @Test
    public void receiveChatMessage_chatMessageGetsSaved(){
        //given
        when(gameRoomHolder.getById(anyString())).thenReturn(testGameRoom);
        ChatMessage chatMessage = new ChatMessage("author3", "content3");

        //when
        chatService.receiveChatMessage("gameId", chatMessage);

        //then
        GameRoom gameRoom = gameRoomHolder.getById("gameId");
        assertEquals(3, gameRoom.getChat().size());
        assertTrue(gameRoom.getChat().contains(chatMessage));
    }

    @Test
    public void receiveChatMessage_chatMessageGetsSentToAll(){
        //given
        when(gameRoomHolder.getById(anyString())).thenReturn(testGameRoom);
        ChatMessage chatMessage = new ChatMessage("author3", "content3");

        //when
        chatService.receiveChatMessage("gameId", chatMessage);

        //then
        verify(messenger, times(1)).sendToAll(anyString(), chatMessageCaptor.capture());
        assertEquals(chatMessage, chatMessageCaptor.getValue());
    }

    @Test
    public void receiveChatMessage_chatMessageGetsCheckedIfWinning(){
        //given
        when(gameRoomHolder.getById(anyString())).thenReturn(testGameRoom);
        ChatMessage chatMessage = new ChatMessage("author3", "content3");

        //when
        chatService.receiveChatMessage("gameId", chatMessage);

        //then
        verify(gameService, times(1)).processPotentialAnswer(anyString(), chatMessageCaptor.capture());
        assertEquals(chatMessage, chatMessageCaptor.getValue());
    }

    @Test
    public void sendAllChatMessages_messagesAreSent(){
        //given
        when(gameRoomHolder.getById(anyString())).thenReturn(testGameRoom);

        //when
        chatService.sendAllChatMessages("gameId", "username");

        //then
        verify(messenger, times(1)).sendAllToUser(anyString(), anyString(), collectionArgumentCaptor.capture());
        assertEquals(testGameRoom.getChat().size(), collectionArgumentCaptor.getValue().size());
    }
}
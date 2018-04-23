package pl.szotaa.punnr.game.service;

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
import pl.szotaa.punnr.game.message.Event;
import pl.szotaa.punnr.game.messenger.Messenger;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

@Category(UnitTest.class)
@RunWith(SpringRunner.class)
public class GameRoomServiceTest {

    @InjectMocks
    private GameRoomService gameRoomService;

    @Mock
    private GameRoomHolder gameRoomHolder;

    @Mock
    private GameService gameService;

    @Mock
    private Messenger messenger;

    private ArgumentCaptor<Event> eventArgumentCaptor = ArgumentCaptor.forClass(Event.class);

    @Test
    public void create_roomGetsCreated(){
        //given
        Map<String, GameRoom> gameRooms = new ConcurrentHashMap<>();
        when(gameRoomHolder.getGameRooms()).thenReturn(gameRooms);

        //when
        gameRoomService.create();

        //then
        verify(gameRoomHolder, times(1)).getGameRooms();
        assertEquals(1, gameRooms.size());
    }

    @Test
    public void join_newPlayer_playerJoinedEventGetsSent() {
        //given
        GameRoom gameRoom = new GameRoom();
        when(gameRoomHolder.getById(anyString())).thenReturn(gameRoom);

        //when
        gameRoomService.join("gameId", "username");

        //then
        verify(messenger, times(1)).sendToAll(anyString(), eventArgumentCaptor.capture());
        assertEquals(Event.EventType.PLAYER_JOINED, eventArgumentCaptor.getValue().getEventType());
    }

    @Test
    public void join_newPlayer_playerInitialScoreIsSet() {
        //given
        GameRoom gameRoom = new GameRoom();
        when(gameRoomHolder.getById(anyString())).thenReturn(gameRoom);

        //when
        gameRoomService.join("gameId", "username");

        //then
        assertEquals(1, gameRoomHolder.getById("gameId").getPlayers().size());
        assertEquals(1, gameRoomHolder.getById("gameId").getScoreboard().size());
        assertEquals(0L, (long) gameRoomHolder.getById("gameId").getScoreboard().get("username"));
    }

    @Test
    public void getAllGames_allGamesReturned(){
        //given
        Map<String, GameRoom> gameRooms = new ConcurrentHashMap<>();
        gameRooms.put("game1", new GameRoom());
        gameRooms.put("game2", new GameRoom());
        when(gameRoomHolder.getGameRooms()).thenReturn(gameRooms);

        //when
        Map<String, GameRoom> result = gameRoomService.getAllGames();

        //then
        verify(gameRoomHolder, times(1)).getGameRooms();
        assertEquals(2, gameRooms.size());
    }
}
package pl.szotaa.punnr.game.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringRunner;
import pl.szotaa.IntegrationTest;
import pl.szotaa.punnr.game.domain.GameRoom;
import pl.szotaa.punnr.game.holder.GameRoomHolder;
import pl.szotaa.punnr.game.message.ChatMessage;
import pl.szotaa.punnr.game.message.Event;
import pl.szotaa.punnr.game.message.Line;
import pl.szotaa.punnr.game.messenger.Messenger;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@Category(IntegrationTest.class)
public class GameServiceTest {

    @InjectMocks
    private GameService gameService;

    @Mock
    private GameRoomHolder gameRoomHolder;

    @Mock
    private Messenger messenger;

    private GameRoom testGameRoom;

    private ArgumentCaptor<Event> eventArgumentCaptor = ArgumentCaptor.forClass(Event.class);

    private ArgumentCaptor<String> stringArgumentCaptor = ArgumentCaptor.forClass(String.class);

    @Before
    public void setUp(){
        testGameRoom = new GameRoom();
        testGameRoom.setCurrentDrawingTitle("correctAnswer");
        testGameRoom.setCurrentDrawer("currentDrawer");
        testGameRoom.getPlayers().addAll(Arrays.asList("currentDrawer", "guesser", "player"));
        testGameRoom.setIterator(testGameRoom.getPlayers().iterator());
        testGameRoom.getScoreboard().put("currentDrawer", 0L);
        testGameRoom.getScoreboard().put("guesser", 0L);
        testGameRoom.getScoreboard().put("player", 0L);
        testGameRoom.getDrawing().add(new Line(0, 0, 100, 100));
    }

    @Test
    public void processPotentialAnswer_answerIncorrect_nothingHappens() throws Exception {
        //given
        when(gameRoomHolder.getById(anyString())).thenReturn(testGameRoom);

        //when
        gameService.processPotentialAnswer("gameId", new ChatMessage("guesser", "incorrectAnswer"));

        //then
        verify(messenger, never()).sendToAll(anyString(), any());
        assertEquals(0L, (long) gameRoomHolder.getById("gameId").getScoreboard().get("currentDrawer"));
        assertEquals(0L, (long) gameRoomHolder.getById("gameId").getScoreboard().get("guesser"));
    }

    @Test
    public void processPotentialAnswer_correctAnswer_roundWinnersScoreIncremented() throws Exception {
        //given
        long WIN_REWARD = 10;
        when(gameRoomHolder.getById(anyString())).thenReturn(testGameRoom);

        //when
        gameService.processPotentialAnswer("gameId", new ChatMessage("guesser", "correctAnswer"));

        //then
        GameRoom gameRoom = gameRoomHolder.getById("gameId");
        assertEquals(WIN_REWARD, (long) gameRoom.getScoreboard().get("currentDrawer"));
        assertEquals(WIN_REWARD, (long) gameRoom.getScoreboard().get("guesser"));
        assertEquals(0L, (long) gameRoom.getScoreboard().get("player"));
    }

    @Test
    public void processPotentialAnswer_correctAnswer_newRoundIsStarted(){
        //given
        when(gameRoomHolder.getById(anyString())).thenReturn(testGameRoom);

        //when
        gameService.processPotentialAnswer("gameId", new ChatMessage("guesser", "correctAnswer"));

        //then
        assertNotEquals("correctAnswer", gameRoomHolder.getById("gameId").getCurrentDrawingTitle());
    }

    @Test
    public void processPotentialAnswer_correctAnswer_roundWonEventSent() {
        //given
        when(gameRoomHolder.getById(anyString())).thenReturn(testGameRoom);

        //when
        gameService.processPotentialAnswer("gameId", new ChatMessage("guesser", "correctAnswer"));

        //then
        verify(messenger, atLeastOnce()).sendToAll(anyString(), eventArgumentCaptor.capture());
        Event event = eventArgumentCaptor.getAllValues().get(0);
        assertEquals("guesser", event.getAuthor());
        assertEquals("correctAnswer", event.getMessage());
        assertEquals(Event.EventType.ROUND_WON, event.getEventType());
    }

    @Test
    public void startNewRound_nextDrawerIsCorrectlyChosen(){
        //given
        when(gameRoomHolder.getById(anyString())).thenReturn(testGameRoom);

        //when&then
        GameRoom gameRoom = gameRoomHolder.getById("gameId");
        String previousDrawer;
        gameService.startNewRound("gameId");
        for(int i = 0; i < 10; i++){
            previousDrawer = gameRoom.getCurrentDrawer();
            gameService.startNewRound("gameId");
            assertNotEquals(previousDrawer, gameRoom.getCurrentDrawer());
            assertNotNull(gameRoom.getCurrentDrawer());
        }
    }

    @Test
    public void startNewRound_drawingTitleIsChanged() {
        //given
        when(gameRoomHolder.getById(anyString())).thenReturn(testGameRoom);

        //when
        gameService.startNewRound("gameId");

        //then
        assertNotEquals("correctAnswer", gameRoomHolder.getById("gameId").getCurrentDrawingTitle());
    }

    @Test
    public void startNewRound_drawingIsCleared(){
        //given
        when(gameRoomHolder.getById(anyString())).thenReturn(testGameRoom);

        //when
        gameService.startNewRound("gameId");

        //then
        assertEquals(0, gameRoomHolder.getById("gameId").getDrawing().size());
    }

    @Test
    public void startNewRound_roundStartedEventSent() {
        //given
        when(gameRoomHolder.getById(anyString())).thenReturn(testGameRoom);

        //when
        gameService.startNewRound("gameId");

        //then
        verify(messenger, times(1)).sendToAll(anyString(), eventArgumentCaptor.capture());
        Event event = eventArgumentCaptor.getValue();
        assertEquals(gameRoomHolder.getById("gameId").getCurrentDrawer(), event.getAuthor());
        assertEquals(Event.EventType.ROUND_STARTED, event.getEventType());
    }

    @Test
    public void startNewRound_youAreDrawingEventSent() {
        //given
        when(gameRoomHolder.getById(anyString())).thenReturn(testGameRoom);

        //when
        gameService.startNewRound("gameId");

        //then
        verify(messenger, times(1)).sendObjectToUser(anyString(), stringArgumentCaptor.capture(), eventArgumentCaptor.capture());
        Event event = eventArgumentCaptor.getValue();
        assertEquals(gameRoomHolder.getById("gameId").getCurrentDrawingTitle(), event.getMessage());
        assertEquals(Event.EventType.YOU_ARE_DRAWING, event.getEventType());
        assertEquals(gameRoomHolder.getById("gameId").getCurrentDrawer(), stringArgumentCaptor.getValue());
    }
}
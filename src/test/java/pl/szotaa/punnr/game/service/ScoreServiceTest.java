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
import pl.szotaa.punnr.game.messenger.Messenger;

import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@Category(UnitTest.class)
public class ScoreServiceTest {

    @InjectMocks
    private ScoreService scoreService;

    @Mock
    private Messenger messenger;

    @Mock
    private GameRoomHolder gameRoomHolder;

    private GameRoom testGameRoom;

    private ArgumentCaptor<Map<?, ?>> argumentCaptor = ArgumentCaptor.forClass(Map.class);

    @Before
    public void setUp(){
        testGameRoom = new GameRoom();
        testGameRoom.setCurrentDrawingTitle("correctAnswer");
        testGameRoom.setCurrentDrawer("currentDrawer");
        testGameRoom.getScoreboard().put("currentDrawer", 0L);
        testGameRoom.getScoreboard().put("player", 0L);
    }

    @Test
    public void sendScoreboard_scoreboardGetsSent() {
        //given
        when(gameRoomHolder.getById(anyString())).thenReturn(testGameRoom);

        //when
        scoreService.sendScoreboard("gameId", "username");

        //then
        verify(messenger, times(1)).sendObjectToUser(anyString(), anyString(), argumentCaptor.capture());
        Map<?, ?> scoreboard = argumentCaptor.getValue();
        assertEquals(2, scoreboard.size());
        assertTrue(scoreboard.containsKey("currentDrawer"));
        assertTrue(scoreboard.containsKey("player"));
    }
}
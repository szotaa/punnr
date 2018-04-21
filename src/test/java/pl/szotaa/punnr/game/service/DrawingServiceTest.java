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
import pl.szotaa.punnr.game.message.Line;
import pl.szotaa.punnr.game.messenger.Messenger;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@Category(UnitTest.class)
public class DrawingServiceTest {

    @InjectMocks
    private DrawingService drawingService;

    @Mock
    private GameRoomHolder gameRoomHolder;

    @Mock
    private Messenger messenger;

    private GameRoom testGameRoom;

    private ArgumentCaptor<Line> lineArgumentCaptor = ArgumentCaptor.forClass(Line.class);

    private ArgumentCaptor<Collection<?>> collectionArgumentCaptor = ArgumentCaptor.forClass(Collection.class);

    @Before
    public void setUp(){
        testGameRoom = new GameRoom();
        testGameRoom.setCurrentDrawingTitle("correctAnswer");
        testGameRoom.setCurrentDrawer("currentDrawer");
        testGameRoom.getDrawing().addAll(Arrays.asList(
                new Line(0, 0, 1, 1),
                new Line(100, 100, 200, 200)
        ));
    }

    @Test
    public void receiveLine_lineGetsSaved(){
        //given
        when(gameRoomHolder.getById(anyString())).thenReturn(testGameRoom);
        Line line = new Line(5, 5, 10, 10);

        //when
        drawingService.receiveLine("gameId", line);

        //then
        assertEquals(3, gameRoomHolder.getById("gameId").getDrawing().size());
    }

    @Test
    public void receiveLine_lineIsSentToAll(){
        //given
        when(gameRoomHolder.getById(anyString())).thenReturn(testGameRoom);
        Line line = new Line(5, 5, 10, 10);

        //when
        drawingService.receiveLine("gameId", line);

        //then
        verify(messenger, times(1)).sendToAll(anyString(), lineArgumentCaptor.capture());
        assertEquals(line, lineArgumentCaptor.getValue());
    }

    @Test
    public void sendAllLines_allLinesAreSent(){
        //given
        when(gameRoomHolder.getById(anyString())).thenReturn(testGameRoom);

        //when
        drawingService.sendAllLines("gameId", "username");

        //then
        verify(messenger, times(1)).sendAllToUser(anyString(), anyString(), collectionArgumentCaptor.capture());
        assertEquals(2, collectionArgumentCaptor.getValue().size());
    }
}
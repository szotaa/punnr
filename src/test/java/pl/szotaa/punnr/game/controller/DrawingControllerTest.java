package pl.szotaa.punnr.game.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.AbstractSubscribableChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.test.context.junit4.SpringRunner;
import pl.szotaa.IntegrationTest;
import pl.szotaa.punnr.game.domain.GameRoom;
import pl.szotaa.punnr.game.holder.GameRoomHolder;
import pl.szotaa.punnr.game.message.Line;
import pl.szotaa.util.MessageHeadersBuilder;

import static org.junit.Assert.assertEquals;


@SpringBootTest
@RunWith(SpringRunner.class)
@Category(IntegrationTest.class)
public class DrawingControllerTest {

    @Autowired
    private GameRoomHolder gameRoomHolder;

    @Autowired
    private AbstractSubscribableChannel clientInboundChannel;

    @Before
    public void setUp() {
        GameRoom gameRoom = new GameRoom();
        gameRoom.setCurrentDrawer("example drawer");
        gameRoom.setCurrentDrawingTitle("example drawing title");
        this.gameRoomHolder.getGameRooms().put("gameId", gameRoom);
    }

    @Test(timeout = 10000L)
    public void sendLine_lineGetsStored() throws Exception {
        //given
        Line line = new Line(0, 0, 1, 1);
        byte[] payload = new ObjectMapper().writeValueAsBytes(line);
        Message<byte[]> message = MessageBuilder.createMessage(payload, MessageHeadersBuilder
                .sendTo("/game/gameId/draw"));

        //when
        this.clientInboundChannel.send(message);

        //then
        Thread.currentThread().sleep(1000L);
        assertEquals(1, gameRoomHolder.getById("gameId").getDrawing().size());
        assertEquals(line, gameRoomHolder.getById("gameId").getDrawing().peek());
    }
}
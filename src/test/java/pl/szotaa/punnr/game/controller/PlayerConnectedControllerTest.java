package pl.szotaa.punnr.game.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.Message;
import org.springframework.messaging.PollableChannel;
import org.springframework.messaging.support.AbstractSubscribableChannel;
import org.springframework.messaging.support.ExecutorSubscribableChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.test.context.junit4.SpringRunner;
import pl.szotaa.IntegrationTest;
import pl.szotaa.punnr.game.domain.GameRoom;
import pl.szotaa.punnr.game.holder.GameRoomHolder;
import pl.szotaa.punnr.game.message.ChatMessage;
import pl.szotaa.util.MessageHeadersBuilder;
import pl.szotaa.util.TestChannelInterceptor;

import static org.junit.Assert.*;

@SpringBootTest
@RunWith(SpringRunner.class)
@Category(IntegrationTest.class)
public class PlayerConnectedControllerTest {

    @Autowired
    private GameRoomHolder gameRoomHolder;

    @Autowired
    private AbstractSubscribableChannel clientInboundChannel;

    @Autowired
    private AbstractSubscribableChannel clientOutboundChannel;

    private TestChannelInterceptor clientOutboundChannelInterceptor;

    private TestChannelInterceptor brokerChannelInterceptor;

    @Before
    public void setUp() {
        GameRoom gameRoom = new GameRoom();
        gameRoom.setCurrentDrawer("example drawer");
        gameRoom.setCurrentDrawingTitle("example drawing title");
        this.gameRoomHolder.getGameRooms().put("gameId", gameRoom);
        this.clientOutboundChannelInterceptor = new TestChannelInterceptor();
        this.brokerChannelInterceptor = new TestChannelInterceptor();
        this.clientOutboundChannel.addInterceptor(clientOutboundChannelInterceptor);
        //this.
    }

    @Test
    public void subscribeToGame_receiveAllChatMessages() throws Exception {
        //given
        gameRoomHolder.getById("gameId").getChat().add(new ChatMessage("user", "content"));

        //when
        Message<byte[]> message = MessageBuilder.createMessage(new byte[0], MessageHeadersBuilder.subscribeTo("/user/queue/gameId"));
        this.clientInboundChannel.send(message);

        //then
        ExecutorSubscribableChannel pollableChannel = (ExecutorSubscribableChannel) clientOutboundChannel;
        //System.out.println(pollableChannel..toString());
    }

}
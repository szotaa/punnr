package pl.szotaa.punnr.game.messenger;

import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import pl.szotaa.UnitTest;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@Category(UnitTest.class)
@RunWith(SpringRunner.class)
public class MessengerTest {

    @InjectMocks
    private Messenger messenger;

    @Mock
    private SimpMessagingTemplate simpMessagingTemplate;

    private ArgumentCaptor<String> stringArgumentCaptor = ArgumentCaptor.forClass(String.class);

    @Test
    public void sendToAll_messageGetsSent(){
        //given
        String gameId = "test-gameid";
        String object = "test-message";

        //when
        messenger.sendToAll(gameId, object);

        //then
        verify(simpMessagingTemplate, times(1)).convertAndSend(stringArgumentCaptor.capture(), stringArgumentCaptor.capture());
        assertEquals("/user/queue/" + gameId, stringArgumentCaptor.getAllValues().get(0));
        assertEquals(object, stringArgumentCaptor.getAllValues().get(1));
    }

    @Test
    public void sendAllToUser_everythingGetsSent(){
        //given
        List<String> messages = Arrays.asList("test-message1", "test-message2", "test-message3");
        String username = "test-username";
        String gameId = "test-gameId";

        //when
        messenger.sendAllToUser(gameId, username, messages);

        //then
        verify(simpMessagingTemplate, times(messages.size())).convertAndSendToUser(
                anyString(), anyString(), stringArgumentCaptor.capture());
        assertEquals(messages, stringArgumentCaptor.getAllValues());
    }

    @Test
    public void sendObjectToUser_objectGetsSent(){
        //given
        String username = "test-username";
        String gameId = "test-gameId";
        String object = "test-message";

        //when
        messenger.sendObjectToUser(gameId, username, object);

        //then
        verify(simpMessagingTemplate, times(1)).convertAndSendToUser(anyString(), anyString(), stringArgumentCaptor.capture());
        assertEquals(object, stringArgumentCaptor.getValue());
    }
}
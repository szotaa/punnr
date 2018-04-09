package pl.szotaa.punnr.game.controller;

import org.junit.Before;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import pl.szotaa.IntegrationTest;
import pl.szotaa.punnr.game.service.ChatService;

@RunWith(SpringRunner.class)
@Category(IntegrationTest.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ChatControllerTest {

    @Value("${local.server.port}")
    private int port;
    private String url;

    @MockBean
    private ChatService chatService;

    @Before
    public void setUp(){
        url = "ws://localhost:" + port + "/game";
    }
}
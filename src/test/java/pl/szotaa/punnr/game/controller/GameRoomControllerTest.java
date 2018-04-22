package pl.szotaa.punnr.game.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import pl.szotaa.IntegrationTest;
import pl.szotaa.punnr.game.service.GameRoomService;

import java.security.Principal;
import java.util.UUID;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@RunWith(SpringRunner.class)
@Category(IntegrationTest.class)
public class GameRoomControllerTest {

    private MockMvc mockMvc;

    @MockBean
    private GameRoomService gameRoomService;

    @Mock
    private Principal principal;

    @Before
    public void setUp(){
        this.mockMvc = MockMvcBuilders
                .standaloneSetup(new GameRoomController(gameRoomService))
                .build();
    }

    @Test
    public void startNewGame_userGetsRedirected() throws Exception {
        mockMvc.perform(get("/room/new"))
                .andExpect(redirectedUrlPattern("/room/**"));
    }

    @Test
    public void startNewGame_newGameGetsCreated() throws Exception {
        mockMvc.perform(get("/room/new"));
        verify(gameRoomService, times(1)).create();
    }

    @Test
    public void joinGame_properViewResolved() throws Exception {
        //given
        String gameId = UUID.randomUUID().toString();

        //when&then
        mockMvc.perform(get("/room/" + gameId).principal(principal))
                .andExpect(status().isOk())
                .andExpect(view().name("room"))
                .andExpect(model().attributeExists("gameId"));
    }

    @Test
    public void getGameRoomList_properViewResolved() throws Exception {
        mockMvc.perform(get("/room").principal(principal))
                .andExpect(status().isOk())
                .andExpect(view().name("rooms"))
                .andExpect(model().attributeExists("rooms"));
    }
}
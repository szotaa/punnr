package pl.szotaa.punnr.user.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import pl.szotaa.IntegrationTest;
import pl.szotaa.punnr.user.domain.User;
import pl.szotaa.punnr.user.service.UserService;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@RunWith(SpringRunner.class)
@Category(IntegrationTest.class)
public class UserControllerTest {

    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    private ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);

    @Before
    public void setUp(){
        this.mockMvc = MockMvcBuilders
                .standaloneSetup(new UserController(userService))
                .build();
    }

    @Test
    public void getRegistrationForm_properViewResolved() throws Exception {
        mockMvc.perform(get("/user/register/"))
                .andExpect(status().isOk())
                .andExpect(view().name("register"))
                .andExpect(model().attributeExists("user"));
    }

    @Test
    public void processRegistrationForm_userGetsPassedToService() throws Exception {
        //given
        User user = User.builder()
                .email("example@email.com")
                .password("examplePassword")
                .build();

        //when&then
        mockMvc.perform(post("/user/register")
                .flashAttr("user", user))
                .andExpect(status().is3xxRedirection());

        verify(userService, times(1)).register(userArgumentCaptor.capture());
        assertEquals(user, userArgumentCaptor.getValue());
    }
}
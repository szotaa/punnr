package pl.szotaa.punnr.user.service;

import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import pl.szotaa.UnitTest;
import pl.szotaa.punnr.user.domain.Role;
import pl.szotaa.punnr.user.domain.User;
import pl.szotaa.punnr.user.repository.UserRepository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@Category(UnitTest.class)
@RunWith(SpringRunner.class)
public class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    private ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);

    @Test
    public void registerNewUser_userGetsSaved() throws Exception {
        //given
        User user = User.builder()
                .email("example@email.com")
                .password("examplePassword")
                .build();

        //when
        userService.register(user);

        //then
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    public void registerNewUser_passwordGetsEncoded() throws Exception {
        //given
        User user = User.builder()
                .email("example@email.com")
                .password("examplePassword")
                .build();

        //when
        userService.register(user);

        //then
        verify(passwordEncoder, times(1)).encode(anyString());
        verify(userRepository, times(1)).save(userArgumentCaptor.capture());
        assertNotEquals("examplePassword", userArgumentCaptor.getValue().getPassword());
    }

    @Test
    public void registerNewUser_roleGetsAttached() throws Exception {
        //given
        User user = User.builder()
                .email("example@email.com")
                .password("examplePassword")
                .build();

        //when
        userService.register(user);

        //then
        verify(userRepository, times(1)).save(userArgumentCaptor.capture());
        assertEquals(Role.ROLE_USER, userArgumentCaptor.getValue().getRole());
    }

    @Test
    public void registerNewUser_userIsSetAsActive() throws Exception {
        //given
        User user = User.builder()
                .email("example@email.com")
                .password("examplePassword")
                .build();

        //when
        userService.register(user);

        //then
        verify(userRepository, times(1)).save(userArgumentCaptor.capture());
        assertTrue(userArgumentCaptor.getValue().getIsActive());
    }
}
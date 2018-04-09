package pl.szotaa;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import pl.szotaa.punnr.config.MvcConfigTest;
import pl.szotaa.punnr.game.controller.GameRoomControllerTest;
import pl.szotaa.punnr.game.service.GameRoomServiceTest;
import pl.szotaa.punnr.user.controller.UserControllerTest;
import pl.szotaa.punnr.user.repository.UserRepositoryTest;
import pl.szotaa.punnr.user.service.UserServiceTest;

@RunWith(Suite.class)
@Suite.SuiteClasses(
        {
                MvcConfigTest.class,
                UserRepositoryTest.class,
                UserServiceTest.class,
                UserControllerTest.class,
                GameRoomServiceTest.class,
                GameRoomControllerTest.class
        }
)
public class AllTestsSuite {
}

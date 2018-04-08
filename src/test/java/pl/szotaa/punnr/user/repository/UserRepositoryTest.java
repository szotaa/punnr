package pl.szotaa.punnr.user.repository;

import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;
import pl.szotaa.IntegrationTest;
import pl.szotaa.punnr.user.domain.User;

import java.util.Optional;

@DataJpaTest
@RunWith(SpringRunner.class)
@Category(IntegrationTest.class)
public class UserRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void findByEmail_userExistent_returnsUser() throws Exception {
        //given
        User user = User.builder()
                .email("example@email.com")
                .build();

        entityManager.persist(user);
        entityManager.flush();

        //when
        Optional<User> found = userRepository.findByEmail("example@email.com");

        //then
        Assert.assertEquals(user, found.get());
    }

    @Test
    public void existsByEmail_userExistent_returnsTrue() throws Exception {
        //given
        User user = User.builder()
                .email("example@email.com")
                .build();

        entityManager.persist(user);
        entityManager.flush();

        //when
        boolean isExistent = userRepository.existsByEmail("example@email.com");

        //then
        Assert.assertTrue(isExistent);
    }
}
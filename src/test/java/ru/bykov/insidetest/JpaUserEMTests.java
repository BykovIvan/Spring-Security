package ru.bykov.insidetest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import ru.bykov.insidetest.model.User;
import ru.bykov.insidetest.repository.UserRepository;

import java.util.HashSet;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.jdbc.EmbeddedDatabaseConnection.H2;

@DataJpaTest
class JpaUserEMTests {

    @Autowired
    private TestEntityManager entityManager;
    @Autowired
    UserRepository repository;


    @Test
    void findByNameTest() {
        User user = new User();
        user.setName("ivan");
        user.setPassword("password");
        user.setRoles(new HashSet<>());
        user.setEmail("ivan@yandex.ru");
        entityManager.persist(user);
        entityManager.flush();

        User userGet = repository.findByName(user.getName()).get();
        assertThat(userGet.getName()).isEqualTo(user.getName());
    }

    @Test
    void existByNameTest() {
        User user = new User();
        user.setName("ivan2");
        user.setPassword("password");
        user.setRoles(new HashSet<>());
        user.setEmail("ivan2@yandex.ru");
        entityManager.persist(user);
        entityManager.flush();

        Boolean userExists = repository.existsByName("ivan2");
        assertThat(userExists).isTrue();

        Boolean userExists2 = repository.existsByName("ivan33");
        assertThat(userExists2).isFalse();

    }

    @Test
    void existByEmailTest() {
        User user = new User();
        user.setName("ivan4");
        user.setPassword("password");
        user.setRoles(new HashSet<>());
        user.setEmail("ivan4@yandex.ru");
        entityManager.persist(user);
        entityManager.flush();

        Boolean userExists = repository.existsByEmail("ivan4@yandex.ru");
        assertThat(userExists).isTrue();

        Boolean userExists2 = repository.existsByEmail("ivan33@mail.ru");
        assertThat(userExists2).isFalse();

    }
}

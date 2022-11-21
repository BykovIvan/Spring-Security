package ru.bykov.insidetest;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import ru.bykov.insidetest.model.Message;
import ru.bykov.insidetest.model.User;
import ru.bykov.insidetest.repository.MessageRepository;
import ru.bykov.insidetest.utils.FromSizeSortPageable;

import java.util.HashSet;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.jdbc.EmbeddedDatabaseConnection.H2;

@RunWith(SpringRunner.class)
@DataJpaTest
@TestPropertySource(properties = {
        "logging.level.org.hibernate.type=trace",
        "logging.level.org.hibernate.sql=debug",
        "logging.level.org.hibernate.type.BasicTypeRegistry=warn",
        "spring.main.banner-mode=off"})
@AutoConfigureTestDatabase(connection = H2, replace = AutoConfigureTestDatabase.Replace.NONE)
class JpaMessageEMTests {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private MessageRepository repository;

    @Test
    void findByNameTest() {
        User user = new User();
        user.setName("ivan");
        user.setUsername("ivan");
        user.setPassword("password");
        user.setRoles(new HashSet<>());
        user.setEmail("ivan@yandex.ru");
        entityManager.persist(user);
        entityManager.flush();
        Message message = new Message();
        message.setUser(user);
        message.setMessage("Hello world");
        entityManager.persist(message);
        entityManager.flush();

        List<Message> messageGet = repository.findByNameLastMessageByLimitOfCount(user.getName(), FromSizeSortPageable.of(0, 10, Sort.by(Sort.Direction.DESC, "dateOfCreate")));
        assertThat(messageGet.get(0).getUser().getName()).isEqualTo(user.getName());
    }

}

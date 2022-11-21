package ru.bykov.insidetest;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;
import ru.bykov.insidetest.model.Message;
import ru.bykov.insidetest.model.User;
import ru.bykov.insidetest.model.dto.MessageDto;
import ru.bykov.insidetest.repository.MessageRepository;
import ru.bykov.insidetest.repository.UserRepository;
import ru.bykov.insidetest.service.MessageService;
import ru.bykov.insidetest.utils.FromSizeSortPageable;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest(
        properties = "db.name=test",
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class MessageServiceTests {

    @InjectMocks
    private final MessageService service;
    @Mock
    private MessageRepository messageRepository;

    @Mock
    private UserRepository userRepository;

    @Test
    void createMessageFromUser() {
        User user = new User();
        user.setName("Ivan35");
        user.setPassword("password");
        user.setEmail("bykov@mail.ru");

        MessageDto messageDto = new MessageDto();
        messageDto.setName(user.getName());
        messageDto.setMessage("Hello world");

        Message message = new Message();
        message.setUser(user);
        message.setMessage(message.getMessage());

        Mockito
                .when(userRepository.existsByName(Mockito.anyString()))
                .thenReturn(false);
        Mockito
                .when(messageRepository.save(Mockito.any(Message.class)))
                .thenReturn(message);

        MessageDto getMessage = service.createByUser(messageDto);

        Assertions.assertEquals(messageDto.getMessage(), getMessage.getMessage());
        Assertions.assertEquals(messageDto.getName(), getMessage.getName());

        Mockito
                .when(userRepository.existsByName(Mockito.anyString()))
                .thenReturn(true);

        final RuntimeException exception = Assertions.assertThrows(
                RuntimeException.class,
                () -> service.createByUser(messageDto));
        Assertions.assertEquals("Данного пользователя не существует", exception.getMessage());


        List<Message> listOfMessage = new ArrayList<>();
        listOfMessage.add(message);

        MessageDto messageDtoHistory = new MessageDto();
        messageDtoHistory.setName(messageDto.getName());
        messageDtoHistory.setMessage("history 10");

        Mockito
                .when(messageRepository.findByNameLastMessageByLimitOfCount(Mockito.anyString(), Mockito.any(FromSizeSortPageable.class)))
                .thenReturn(listOfMessage);

        List<MessageDto> listOfDtoMessage = service.getMessagesByUser(messageDtoHistory);

        Assertions.assertEquals(messageDto.getMessage(), listOfDtoMessage.get(0).getMessage());
        Assertions.assertEquals(messageDto.getName(), listOfDtoMessage.get(0).getName());

        Mockito
                .when(userRepository.existsByName(Mockito.anyString()))
                .thenReturn(true);

        final RuntimeException exception2 = Assertions.assertThrows(
                RuntimeException.class,
                () -> service.getMessagesByUser(messageDto));
        Assertions.assertEquals("Данного пользователя не существует", exception2.getMessage());

    }

}

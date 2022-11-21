package ru.bykov.insidetest.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.bykov.insidetest.model.Message;
import ru.bykov.insidetest.model.dto.MessageDto;
import ru.bykov.insidetest.repository.MessageRepository;
import ru.bykov.insidetest.repository.UserRepository;
import ru.bykov.insidetest.service.MessageService;
import ru.bykov.insidetest.utils.FromSizeSortPageable;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {

    private final MessageRepository messageRepository;
    private final UserRepository userRepository;

    @Override
    public MessageDto createByUser(MessageDto messageDto) {
        if (!userRepository.existsByName(messageDto.getName()))
            throw new RuntimeException("Данного пользователя не существует");
        Message newMessage = mapToEntity(messageDto);
        Message message = messageRepository.save(newMessage);
        return mapToDto(message);
    }

    @Override
    public List<MessageDto> getMessagesByUser(MessageDto messageDto) {
        if (!userRepository.existsByName(messageDto.getName()))
            throw new RuntimeException("Данного пользователя не существует");
        if (messageDto.getMessage().startsWith("history ")) {
            int count = Integer.parseInt(messageDto.getMessage().split(" ")[1]);
            List<Message> listOfMessage = messageRepository.findByNameLastMessageByLimitOfCount(messageDto.getName(), FromSizeSortPageable.of(0, count, Sort.by(Sort.Direction.DESC, "dateOfCreate")));
            return listOfMessage.stream().map(this::mapToDto).collect(Collectors.toList());
        }
        return new ArrayList<>();

    }

    private Message mapToEntity(MessageDto messageDto) {
        Message message = new Message();
        message.setUser(userRepository.findByName(messageDto.getName()).get());
        message.setMessage(messageDto.getMessage());
        message.setDateOfCreate(LocalDateTime.now());
        return message;
    }

    private MessageDto mapToDto(Message message) {
        MessageDto messageDto = new MessageDto();
        messageDto.setName(message.getUser().getName());
        messageDto.setMessage(message.getMessage());
        return messageDto;
    }
}

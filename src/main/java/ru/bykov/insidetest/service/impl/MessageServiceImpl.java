package ru.bykov.insidetest.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.bykov.insidetest.model.Message;
import ru.bykov.insidetest.model.dto.MessageDto;
import ru.bykov.insidetest.repository.MessageRepository;
import ru.bykov.insidetest.service.MessageService;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {

    private final MessageRepository messageRepository;

    @Override
    public MessageDto createByUser(MessageDto messageDto) {
        Message newMessage = mapToEntity(messageDto);
        Message message = messageRepository.save(newMessage);
        return mapToDto(message);
    }

    private Message mapToEntity(MessageDto messageDto) {
        Message message = new Message();
        message.setName(messageDto.getName());
        message.setMessage(messageDto.getMessage());
        message.setDateOfCreate(LocalDateTime.now());
        return message;
    }

    private MessageDto mapToDto(Message message) {
        MessageDto messageDto = new MessageDto();
        messageDto.setName(message.getName());
        messageDto.setMessage(message.getMessage());
        return messageDto;
    }
}

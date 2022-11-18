package ru.bykov.insidetest.service;

import ru.bykov.insidetest.model.dto.MessageDto;

import java.util.List;

public interface MessageService {
    MessageDto createByUser(MessageDto messageDto);

    List<MessageDto> getMessagesByUser(MessageDto messageDto);
}

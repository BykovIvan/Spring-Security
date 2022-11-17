package ru.bykov.insidetest.service;

import ru.bykov.insidetest.model.dto.MessageDto;

public interface MessageService {
    MessageDto createByUser(MessageDto messageDto);
}

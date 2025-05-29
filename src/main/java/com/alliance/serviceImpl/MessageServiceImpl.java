package com.alliance.service.impl;

import com.alliance.model.Message;
import com.alliance.repository.MessageRepository;
import com.alliance.service.MessageService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageServiceImpl implements MessageService {

    private final MessageRepository messageRepository;

    public MessageServiceImpl(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    @Override
    public List<Message> getMessagesForEmployee(String employeeId) {
        return messageRepository.findByEmployeeCode(employeeId);
    }
}
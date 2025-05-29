package com.alliance.service;

import com.alliance.model.Message;
import java.util.List;

public interface MessageService {
    List<Message> getMessagesForEmployee(String employeeId);
}
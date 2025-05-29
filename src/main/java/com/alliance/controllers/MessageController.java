package com.alliance.controller;

import com.alliance.model.Message;
import com.alliance.service.MessageService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/messages")
public class MessageController {

    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @GetMapping("/employee/{employeeId}")
    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity<List<Message>> getMessagesForEmployee(
            @PathVariable String employeeId) {
        return ResponseEntity.ok(messageService.getMessagesForEmployee(employeeId));
    }
}
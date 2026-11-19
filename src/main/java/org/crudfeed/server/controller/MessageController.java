package org.crudfeed.server.controller;

import org.crudfeed.core.dto.*;
import org.crudfeed.core.entity.Message;
import org.crudfeed.core.service.MessageService;
import org.crudfeed.core.service.UserService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Transactional
@RestController
@RequestMapping("/api")
public class MessageController {

    private final MessageService messageService;
    private final UserService userService;

    public MessageController(MessageService messageService, UserService userService) {
        this.messageService = messageService;
        this.userService = userService;
    }

    @PostMapping("/writeMessage")
    public String writeMessage(@RequestBody WriteMessageRequestDto dto) {
        try {
            messageService.sendMessage(dto.senderId(), dto.receiverId(), dto.content());
            return "Successfully sent!";
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    @PostMapping("/viewMessages")
    public List<Message> viewMessages(@RequestBody ViewMessagesRequestDto dto) {
        try {
            long receiverId = userService.getUserId(dto.receiverName());
            return messageService.viewMessages(dto.senderId(), receiverId);
        } catch (Exception e) {
            return null;
        }
    }

    @PostMapping("/editMessage")
    public String editMessage(EditMessageRequestDto dto) {
        try {
            messageService.editMessage(dto.messageId(), dto.content(), dto.senderId());
            return "Successfully edited!";
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    @PostMapping("/deleteMessage")
    public String deleteMessage(@RequestBody DeleteMessageRequestDto dto) {
        try {
            messageService.deleteMessage(dto.messageId(), dto.senderId());
            return "Successfully deleted!";
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }
}
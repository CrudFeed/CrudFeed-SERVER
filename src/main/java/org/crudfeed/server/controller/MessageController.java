package org.crudfeed.server.controller;

import org.crudfeed.core.dto.message.ViewMessagesRequestDto;
import org.crudfeed.core.dto.message.DeleteMessageRequestDto;
import org.crudfeed.core.dto.message.EditMessageRequestDto;
import org.crudfeed.core.dto.message.WriteMessageRequestDto;
import org.crudfeed.core.entity.Message;
import org.crudfeed.core.service.MessageService;
import org.crudfeed.core.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
            long receiverId = userService.getUserId(dto.receiverNickname());
            return messageService.sendMessage(dto.senderId(), receiverId, dto.content());
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
            return messageService.editMessage(dto.messageId(), dto.content(), dto.senderId());
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    @PostMapping("/deleteMessage")
    public String deleteMessage(@RequestBody DeleteMessageRequestDto dto) {
        try {
            return messageService.deleteMessage(dto.messageId(), dto.senderId());
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }
}
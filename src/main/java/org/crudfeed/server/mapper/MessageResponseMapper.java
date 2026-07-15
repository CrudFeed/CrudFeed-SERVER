package org.crudfeed.server.mapper;

import org.crudfeed.core.entity.Message;

import java.util.List;

public class MessageResponseMapper {

    public String map(List<Message> messages) {
        StringBuilder result = new StringBuilder();

        for (Message message : messages) {

            result.append(message.getId()).append("|")
                    .append(message.getSenderId()).append("|")
                    .append(message.getReceiverId()).append("|")
                    .append(message.getText().replace("|", "/")).append("|")
                    .append(message.getCreatedAt())
                    .append("\n");
        }

        return result.toString();
    }
}
package org.crudfeed.server.mapper;

import org.crudfeed.core.entity.Post;

import java.util.List;

public class PostResponseMapper {

    public String map(List<Post> posts) {
        StringBuilder result = new StringBuilder();

        for (Post post : posts) {
            result.append(post.getId()).append("|")
                    .append(post.getUser().getNickname()).append("|")
                    .append(post.getText().replace("|", "/")).append("|")
                    .append(post.getLikes())
                    .append("\n");
        }

        return result.toString();
    }
}
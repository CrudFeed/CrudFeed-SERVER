package org.crudfeed.server.mapper;

import org.crudfeed.core.entity.User;
import org.crudfeed.core.service.UserService;

public class AccountResponseMapper {

    private final UserService userService;

    public AccountResponseMapper(UserService userService) {
        this.userService = userService;
    }

    public String map(User user) {

        if (user == null) {
            return "EMPTY";
        }

        long subscribers = userService.getSubscribersCount(user.getId());
        long subscriptions = userService.getSubscriptionsCount(user.getId());

        return user.getId() + "|"
                + user.getNickname() + "|"
                + subscribers + "|"
                + subscriptions + "|"
                + user.getBio().replace("|", "/");
    }
}
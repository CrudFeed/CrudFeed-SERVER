package org.crudfeed.server.controller;

import org.crudfeed.core.dto.account.*;
import org.crudfeed.core.dto.account.AuthResultResponseDto;
import org.crudfeed.core.dto.account.SignInRequestDto;
import org.crudfeed.core.dto.account.SignUpRequestDto;
import org.crudfeed.core.dto.account.FriendRequestDto;
import org.crudfeed.core.service.AuthService;
import org.crudfeed.core.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class UserController {

    private final AuthService authService;
    private final UserService userService;

    public UserController(AuthService authService, UserService userService) {
        this.authService = authService;
        this.userService = userService;
    }

    @PostMapping("/signUp")
    public AuthResultResponseDto signUp(@RequestBody SignUpRequestDto dto) {
        return authService.signUp(dto.email(), dto.nickname(), dto.displayName(), dto.password());
    }

    @PostMapping("/signIn")
    public AuthResultResponseDto signIn(@RequestBody SignInRequestDto dto) {
        return authService.signIn(dto.email(), dto.password());
    }

    @PostMapping("/getBanned")
    public GetBannedRequestDto getBanned(@RequestBody GetBannedRequestDto dto) {
        return userService.getBanned(dto.userId());
    }

    @GetMapping("/status")
    public String status() {
        return "ONLINE";
    }

    @PostMapping("/subscribe")
    public String subscribe(@RequestBody SubscribeRequestDto dto) {
        try {
            return userService.subscribe(dto.userId(), dto.nickname());
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    @PostMapping("/unsubscribe")
    public String unsubscribe(@RequestBody SubscribeRequestDto dto) {
        try {
            return userService.unsubscribe(dto.userId(), dto.nickname());
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    @PostMapping("/addFriend")
    public String addFriend(@RequestBody FriendRequestDto dto) {
        try {
            return userService.addOrRemoveFriend(dto.userId(), dto.friendName(), "add");
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    @PostMapping("/removeFriend")
    public String removeFriend(@RequestBody FriendRequestDto dto) {
        try {
            return userService.addOrRemoveFriend(dto.userId(), dto.friendName(), "remove");
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    @PostMapping("/showFriendsList")
    public List<ShowFriendsListRequestDto> showFriendsList(@RequestBody ShowFriendsListRequestDto dto) {
        return userService.showFriendsList(dto.userId());
    }

    @PostMapping("/viewAccountPage")
    public Object viewAccountPage(@RequestBody ViewAccountPageRequestDto dto) {
        try {
            return userService.viewAccountPage(dto.nickname());
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    @PostMapping("/editAccountPage")
    public String editAccountPage(@RequestBody EditAccountPageRequestDto dto) {
        try {
            return userService.editAccountPage(dto.userId(), dto.content());
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    @PostMapping("/changeNickname")
    public String changeNickname(@RequestBody ChangeNicknameRequestDto dto) {
        try {
            return userService.changeName(dto.nickname(), dto.userId());
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    @PostMapping("/changePassword")
    public String changePassword(@RequestBody ChangePasswordRequestDto dto) {
        try {
            return userService.changePass(dto.password(), dto.userId());
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    @PostMapping("/deleteAccount")
    public String deleteAccount(@RequestBody DeleteAccountRequestDto dto) {
        try {
            return userService.deleteAccount("y", dto.userId(), dto.email(), dto.nickname());
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    @PostMapping("/getUserById")
    public String getNicknameById(@RequestBody AccountIdRequestDto dto) {
        try {
            return userService.getNicknameById(dto.userId());
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }
}
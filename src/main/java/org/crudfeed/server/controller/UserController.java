package org.crudfeed.server.controller;

import org.crudfeed.core.dto.*;
import org.crudfeed.core.entity.User;
import org.crudfeed.core.service.AuthService;
import org.crudfeed.core.service.UserService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class UserController {

    private final AuthService authService;
    private final UserService userService;

    public UserController(AuthService authService, UserService userService) {
        this.authService = authService;
        this.userService = userService;
    }

    @PostMapping("/sign-up")
    public User signUp(@RequestBody SignUpRequestDto dto) {
        return authService.signUp(dto.email(), dto.nickname(), dto.displayName(), dto.password());
    }

    @PostMapping("/sign-in")
    public UserResponseDto signIn(@RequestBody SignInRequestDto dto) {
        return authService.signIn(dto.email(), dto.password());
    }

    @GetMapping("/status")
    public String status() {
        return "ONLINE";
    }

    @PostMapping("/subscribe")
    public String subscribe(@RequestBody SubscribeRequestDto dto) {
        try {
            userService.subscribe(dto.userId(), dto.nickname());
            return "Successfully subscribed!";
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    @PostMapping("/unsubscribe")
    public String unsubscribe(@RequestBody SubscribeRequestDto dto) {
        try {
            userService.unsubscribe(dto.userId(), dto.nickname());
            return "Successfully unsubscribed!";
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
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
            userService.editAccountPage(dto.userId(), dto.content());
            return "Successfully changed!";
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    @PostMapping("/changeNickname")
    public String changeNickname(@RequestBody ChangeNicknameRequestDto dto) {
        try {
            userService.changeName(dto.nickname(), dto.userId());
            return "Successfully changed!";
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    @PostMapping("/changePassword")
    public String changePassword(@RequestBody ChangePasswordRequestDto dto) {
        try {
            userService.changePass(dto.password(), dto.userId());
            return "Successfully changed!";
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    @PostMapping("/deleteAccount")
    public String deleteAccount(@RequestBody AccountIdRequestDto dto) {
        try {
            userService.deleteAccount("y", dto.userId());
            return "Successfully deleted!";
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
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
    public AuthResultDto signUp(@RequestBody SignUpRequestDto dto) {
        return authService.signUp(dto.email(), dto.nickname(), dto.displayName(), dto.password());
    }

    @PostMapping("/sign-in")
    public AuthResultDto signIn(@RequestBody SignInRequestDto dto) {
        return authService.signIn(dto.email(), dto.password());
    }

    @PostMapping("/getBanned")
    public GetBannedDtoRequest getBanned(@RequestBody GetBannedDtoRequest dto) {
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
    public String deleteAccount(@RequestBody AccountIdRequestDto dto) {
        try {
            return userService.deleteAccount("y", dto.userId());
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

    @PostMapping("/modBanAccount")
    public String modBanAccount(@RequestBody long userId) {
        try {
            return userService.modBanAccount(userId);
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }
    @PostMapping("/modUnbanAccount")
    public String modUnbanAccount(@RequestBody long userId) {
        try {
            return userService.modUnbanAccount(userId);
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

}
package org.crudfeed.server.controller;

import org.crudfeed.core.dto.account.*;
import org.crudfeed.core.dto.account.AuthResultResponseDto;
import org.crudfeed.core.dto.account.SignInRequestDto;
import org.crudfeed.core.dto.account.SignUpRequestDto;
import org.crudfeed.core.dto.account.FriendRequestDto;
import org.crudfeed.core.service.AuthService;
import org.crudfeed.core.service.UserService;
import org.crudfeed.server.security.TokenStore;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class UserController {

    private final AuthService authService;
    private final UserService userService;
    private final TokenStore tokenStore;

    public UserController(AuthService authService, UserService userService, TokenStore tokenStore) {
        this.authService = authService;
        this.userService = userService;
        this.tokenStore = tokenStore;
    }

    @PostMapping("/signUp")
    public ResponseEntity<AuthResultResponseDto> signUp(@RequestBody SignUpRequestDto dto) {
        AuthResultResponseDto result = authService.signUp(
                dto.email(), dto.nickname(), dto.displayName(), dto.password()
        );
        if (!result.ok() || result.user() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }
        return tokenResponse(result);
    }

    @PostMapping("/signIn")
    public ResponseEntity<AuthResultResponseDto> signIn(@RequestBody SignInRequestDto dto) {
        AuthResultResponseDto result = authService.signIn(dto.email(), dto.password());
        if (!result.ok() || result.user() == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(result);
        }
        return tokenResponse(result);
    }

    private ResponseEntity<AuthResultResponseDto> tokenResponse(AuthResultResponseDto result) {
        String token = tokenStore.createToken(result.user().id());
        return ResponseEntity.ok()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .body(result);
    }

    @PostMapping("/getBanned")
    public GetBannedDto getBanned(@RequestBody GetBannedDto dto) {
        return userService.getBanned(dto.userId());
    }

    @GetMapping("/status")
    public String status() {
        return "ONLINE";
    }

    @PostMapping("/subscribe")
    public String subscribe(
            @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String authHeader,
            @RequestBody SubscribeRequestDto dto
    ) {
        try {
            long userId = tokenStore.requireUserId(authHeader);
            return userService.subscribe(userId, dto.nickname());
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    @PostMapping("/unsubscribe")
    public String unsubscribe(
            @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String authHeader,
            @RequestBody SubscribeRequestDto dto
    ) {
        try {
            long userId = tokenStore.requireUserId(authHeader);
            return userService.unsubscribe(userId, dto.nickname());
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    @PostMapping("/addFriend")
    public String addFriend(
            @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String authHeader,
            @RequestBody FriendRequestDto dto) {
        try {
            long userId = tokenStore.requireUserId(authHeader);
            return userService.addOrRemoveFriend(userId, dto.friendName(), "add");
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    @PostMapping("/removeFriend")
    public String removeFriend(
            @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String authHeader,
            @RequestBody FriendRequestDto dto) {
        try {
            long userId = tokenStore.requireUserId(authHeader);
            return userService.addOrRemoveFriend(userId, dto.friendName(), "remove");
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    @PostMapping("/showFriendsList")
    public List<ShowFriendsListDto> showFriendsList(
            @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String authHeader) {

        long userId = tokenStore.requireUserId(authHeader);
        return userService.showFriendsList(userId);
    }

    @PostMapping("/viewAccountPage")
    public Object viewAccountPage(@RequestBody ViewAccountPageDto dto) {
        try {
            return userService.viewAccountPage(dto.nickname());
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    @PostMapping("/editAccountPage")
    public String editAccountPage(
            @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String authHeader,
            @RequestBody EditAccountPageRequestDto dto) {
        try {
            long userId = tokenStore.requireUserId(authHeader);
            return userService.editAccountPage(userId, dto.content());
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    @PostMapping("/changeNickname")
    public String changeNickname(
            @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String authHeader,
            @RequestBody ChangeNicknameRequestDto dto) {
        try {
            long userId = tokenStore.requireUserId(authHeader);
            return userService.changeName(dto.nickname(), userId);
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    @PostMapping("/changePassword")
    public String changePassword(
            @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String authHeader,
            @RequestBody ChangePasswordRequestDto dto) {
        try {
            long userId = tokenStore.requireUserId(authHeader);
            return userService.changePass(dto.password(), userId);
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    @PostMapping("/deleteAccount")
    public String deleteAccount(
            @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String authHeader,
            @RequestBody DeleteAccountRequestDto dto) {
        try {
            long userId = tokenStore.requireUserId(authHeader);
            return userService.deleteAccount("y", userId, dto.email(), dto.nickname());
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
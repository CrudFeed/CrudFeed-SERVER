package org.crudfeed.server.controller;

import org.crudfeed.core.dto.mod.ModActionRoleResultResponseDto;
import org.crudfeed.core.dto.mod.ModInfoRequestDto;
import org.crudfeed.core.dto.mod.ModShowResponseDto;
import org.crudfeed.core.service.ModService;
import org.crudfeed.server.security.TokenStore;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class ModController {

    private final ModService modService;
    private final TokenStore tokenStore;

    public ModController(ModService modService, TokenStore tokenStore) {
        this.modService = modService;
        this.tokenStore = tokenStore;
    }

    @PostMapping("/modBanAccount")
    public String modBanAccount(
            @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String authHeader,
            @RequestBody ModInfoRequestDto dto) {
        try {
            long modId = tokenStore.requireUserId(authHeader);
            return modService.modBanOrUnbanAccount(modId, dto.targetId(), "ban");
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }
    @PostMapping("/modUnbanAccount")
    public String modUnbanAccount(
            @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String authHeader,
            @RequestBody ModInfoRequestDto dto) {
        try {
            long modId = tokenStore.requireUserId(authHeader);
            return modService.modBanOrUnbanAccount(modId, dto.targetId(), "unban");
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    @PostMapping("/modDeleteAccount")
    public String modDeleteAccount(
            @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String authHeader,
            @RequestBody ModInfoRequestDto dto) {
        try {
            long modId = tokenStore.requireUserId(authHeader);
            return modService.modDeleteOrRestoreAccount(modId, dto.targetId(), "delete");
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    @PostMapping("/modRestoreAccount")
    public String modRestoreAccount(
            @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String authHeader,
            @RequestBody ModInfoRequestDto dto) {
        try {
            long modId = tokenStore.requireUserId(authHeader);
            return modService.modDeleteOrRestoreAccount(modId, dto.targetId(),  "restore");
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    @PostMapping("/modPromoteAccount")
    public ModActionRoleResultResponseDto modPromoteAccount(
            @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String authHeader,
            @RequestBody ModInfoRequestDto dto) {
        long modId = tokenStore.requireUserId(authHeader);
        return modService.modPromoteOrDemoteAccount(modId, dto.targetId(),  "promote");
    }

    @PostMapping("/modDemoteAccount")
    public ModActionRoleResultResponseDto modDemoteAccount(
            @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String authHeader,
            @RequestBody ModInfoRequestDto dto) {
        long modId = tokenStore.requireUserId(authHeader);
        return modService.modPromoteOrDemoteAccount(modId, dto.targetId(),  "demote");
    }

    @PostMapping("/modDeletePost")
    public String modDeletePost(
            @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String authHeader,
            @RequestBody ModInfoRequestDto dto) {
        try {
            long modId = tokenStore.requireUserId(authHeader);
            return modService.modDeleteOrRestorePost(modId, dto.targetId(), "delete");
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    @PostMapping("/modRestorePost")
    public String modRestorePost(
            @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String authHeader,
            @RequestBody ModInfoRequestDto dto) {
        try {
            long modId = tokenStore.requireUserId(authHeader);
            return modService.modDeleteOrRestorePost(modId, dto.targetId(),  "restore");
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    @PostMapping("/modShow")
    public ModShowResponseDto modShow(
            @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String authHeader,
            @RequestBody ModInfoRequestDto dto) {
        long modId = tokenStore.requireUserId(authHeader);
        return modService.modShow(modId, dto.targetId());
    }
}
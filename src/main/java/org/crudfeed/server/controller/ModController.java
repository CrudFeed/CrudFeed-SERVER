package org.crudfeed.server.controller;

import org.crudfeed.core.dto.mod.ModActionRoleResultResponseDto;
import org.crudfeed.core.dto.mod.ModInfoRequestDto;
import org.crudfeed.core.dto.mod.ModShowResponseDto;
import org.crudfeed.core.service.ModService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class ModController {

    private final ModService modService;

    public ModController(ModService modService) {
        this.modService = modService;
    }

    @PostMapping("/modBanAccount")
    public String modBanAccount(@RequestBody ModInfoRequestDto dto) {
        try {
            return modService.modBanOrUnbanAccount(dto.modId(), dto.targetId(), "ban");
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }
    @PostMapping("/modUnbanAccount")
    public String modUnbanAccount(@RequestBody ModInfoRequestDto dto) {
        try {
            return modService.modBanOrUnbanAccount(dto.modId(), dto.targetId(), "unban");
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    @PostMapping("/modDeleteAccount")
    public String modDeleteAccount(@RequestBody ModInfoRequestDto dto) {
        try {
            return modService.modDeleteOrRestoreAccount(dto.modId(), dto.targetId(), "delete");
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    @PostMapping("/modRestoreAccount")
    public String modRestoreAccount(@RequestBody ModInfoRequestDto dto) {
        try {
            return modService.modDeleteOrRestoreAccount(dto.modId(), dto.targetId(),  "restore");
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    @PostMapping("/modPromoteAccount")
    public ModActionRoleResultResponseDto modPromoteAccount(@RequestBody ModInfoRequestDto dto) {
        return modService.modPromoteOrDemoteAccount(dto.modId(), dto.targetId(),  "promote");
    }

    @PostMapping("/modDemoteAccount")
    public ModActionRoleResultResponseDto modDemoteAccount(@RequestBody ModInfoRequestDto dto) {
        return modService.modPromoteOrDemoteAccount(dto.modId(), dto.targetId(),  "demote");
    }

    @PostMapping("/modDeletePost")
    public String modDeletePost(@RequestBody ModInfoRequestDto dto) {
        try {
            return modService.modDeleteOrRestorePost(dto.modId(), dto.targetId(), "delete");
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    @PostMapping("/modRestorePost")
    public String modRestorePost(@RequestBody ModInfoRequestDto dto) {
        try {
            return modService.modDeleteOrRestorePost(dto.modId(), dto.targetId(),  "restore");
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    @PostMapping("/modShow")
    public ModShowResponseDto modShow(@RequestBody ModInfoRequestDto dto) {
        return modService.modShow(dto.modId(), dto.targetId());
    }
}
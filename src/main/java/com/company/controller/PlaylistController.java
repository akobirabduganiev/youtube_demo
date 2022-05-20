package com.company.controller;

import com.company.dto.ChangePlaylistDTO;
import com.company.dto.ChangePlaylistStatusDTO;
import com.company.dto.PlaylistDTO;
import com.company.enums.ProfileRole;
import com.company.service.PlaylistService;
import com.company.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping("playlist/")
public class PlaylistController {
    @Autowired
    PlaylistService playlistService;

    @PostMapping("/profile/create")
    public ResponseEntity<PlaylistDTO> create(@RequestBody PlaylistDTO dto,
                                              HttpServletRequest request) {
        var id = JwtUtil.getIdFromHeader(request);
        return ResponseEntity.ok(playlistService.create(dto, id));
    }

    @PutMapping("/profile/update")
    public ResponseEntity<?> update(@RequestBody @Valid ChangePlaylistDTO dto,
                                    HttpServletRequest request) {
        var id = JwtUtil.getIdFromHeader(request);

        return ResponseEntity.ok(playlistService.update(dto, id));
    }

    @PutMapping("/profile/change-status")
    public ResponseEntity<?> changeStatus(@RequestBody @Valid ChangePlaylistStatusDTO dto,
                                          HttpServletRequest request) {
        var id = JwtUtil.getIdFromHeader(request);
        return ResponseEntity.ok(playlistService.changeStatus(dto, id));
    }

    @DeleteMapping("profile/delete/{id}")
    public ResponseEntity<?> deleteById(@PathVariable("id") Integer playlistId,
                                        HttpServletRequest request) {
        var id = JwtUtil.getIdFromHeader(request);
        return ResponseEntity.ok(playlistService.deleteById(playlistId, id));
    }

    @GetMapping("/adm/list")
    public ResponseEntity<?> list(@RequestParam(value = "page", defaultValue = "0") int page, @RequestParam(value = "size", defaultValue = "5") int size, HttpServletRequest request) {
        JwtUtil.getIdFromHeader(request, ProfileRole.ADMIN);
        return ResponseEntity.ok(playlistService.admPlaylist(page, size));
    }

    @GetMapping("/list/{channelId}")
    public ResponseEntity<?> channelPlaylist(@PathVariable("channelId") String channelId) {
        return ResponseEntity.ok(playlistService.channelPlaylist(channelId));
    }

    @GetMapping("/{playlistId}")
    public ResponseEntity<?> get(@PathVariable("playlistId") Integer playlistId) {
        return ResponseEntity.ok(playlistService.get(playlistId));
    }
}

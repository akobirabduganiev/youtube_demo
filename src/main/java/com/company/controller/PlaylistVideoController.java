package com.company.controller;

import com.company.dto.PlaylistVideoDTO;
import com.company.dto.UpdateOrderNumDTO;
import com.company.service.PlaylistVideoService;
import com.company.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping("playlist-video/")
@RequiredArgsConstructor
@Slf4j
public class PlaylistVideoController {

    private final PlaylistVideoService playlistVideoService;

    @PostMapping("/profile/create")
    public ResponseEntity<PlaylistVideoDTO> create(@RequestBody @Valid PlaylistVideoDTO dto,
                                                   HttpServletRequest request) {
        Integer id = JwtUtil.getIdFromHeader(request);
        log.info("/profile/{playlistVideoId} {}", dto);
        return ResponseEntity.ok(playlistVideoService.create(dto, id));
    }


    @PutMapping("/profile/{playlistVideoId}")
    public ResponseEntity<?> update(@RequestBody @Valid UpdateOrderNumDTO dto,
                                    @PathVariable("playlistVideoId") Integer playlistVideoId,
                                    HttpServletRequest request) {
        Integer id = JwtUtil.getIdFromHeader(request);
        log.info("/profile/{playlistVideoId} {}", dto);
        return ResponseEntity.ok(playlistVideoService.update(dto, playlistVideoId, id));
    }

    @GetMapping("/{playlistVideoId}")
    public ResponseEntity<?> get(@PathVariable("playlistVideoId") Integer playlistVideoId) {
        log.info("/{playlistVideoId} {}", playlistVideoId);
        return ResponseEntity.ok(playlistVideoService.get(playlistVideoId));
    }

    @GetMapping("/list/{playlistVideoId}")
    public ResponseEntity<?> videoList(@PathVariable("playlistVideoId") Integer playlistVideoId) {
        log.info("list {}", playlistVideoId);
        return ResponseEntity.ok(playlistVideoService.videosByPlaylistId(playlistVideoId));
    }
}

package com.company.controller;

import com.company.dto.VideoTagDTO;
import com.company.service.VideoTagService;
import com.company.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("video-tag/")
public class VideoTagController {
    private final VideoTagService videoTagService;

    @PostMapping("/profile/create")
    public ResponseEntity<VideoTagDTO> create(@RequestBody @Valid VideoTagDTO dto,
                                    HttpServletRequest request) {
        Integer id = JwtUtil.getIdFromHeader(request);

        return ResponseEntity.ok(videoTagService.create(dto, id));
    }
}

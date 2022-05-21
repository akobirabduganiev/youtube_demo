package com.company.controller;

import com.company.dto.ChangeVideoDetailDTO;
import com.company.dto.ChangeVideoStatusDTO;
import com.company.dto.VideoDTO;
import com.company.dto.VideoTitleDTO;
import com.company.enums.ProfileRole;
import com.company.service.VideoService;
import com.company.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping("video/")
public class VideoController {
    @Autowired
    private VideoService videoService;

    @PostMapping("/profile/create")
    public ResponseEntity<VideoDTO> create(@RequestBody @Valid VideoDTO dto,
                                           HttpServletRequest request) {
        Integer id = JwtUtil.getIdFromHeader(request);
        return ResponseEntity.ok(videoService.create(dto, id));
    }

    @GetMapping("/adm/list")
    public ResponseEntity<?> list(@RequestParam(value = "page", defaultValue = "0") int page,
                                  @RequestParam(value = "size", defaultValue = "3") int size,
                                  HttpServletRequest request) {
        JwtUtil.getIdFromHeader(request, ProfileRole.ADMIN);

        return ResponseEntity.ok(videoService.paginationList(page, size));
    }

    @GetMapping("/list-by-category/{categoryId}")
    public ResponseEntity<?> listByCategory(@PathVariable("categoryId") Integer categoryId,
                                            @RequestParam(value = "page", defaultValue = "0") int page,
                                            @RequestParam(value = "size", defaultValue = "3") int size) {
        return ResponseEntity.ok(videoService.listByCategory(categoryId, page, size));
    }

    @PostMapping("/profile/get-video/{key}")
    public ResponseEntity<?> getVideoByKey(@PathVariable("key") String key,
                                           HttpServletRequest request) {
        Integer id = JwtUtil.getIdFromHeader(request);

        return ResponseEntity.ok(videoService.getVideoByKey(key, id));
    }

    @PutMapping("/profile/update-detail")
    public ResponseEntity<?> updateDetail(@RequestBody @Valid ChangeVideoDetailDTO dto,
                                          HttpServletRequest request) {
        Integer id = JwtUtil.getIdFromHeader(request);

        return ResponseEntity.ok(videoService.updateDetail(dto, id));
    }

    @PutMapping("/profile/update-status")
    public ResponseEntity<?> updateStatus(@RequestBody @Valid ChangeVideoStatusDTO dto,
                                          HttpServletRequest request) {
        Integer id = JwtUtil.getIdFromHeader(request);

        return ResponseEntity.ok(videoService.changeVideoStatus(dto, id));
    }

    @PostMapping("/search-by-title")
    public ResponseEntity<?> searchByTitle(@RequestBody VideoTitleDTO dto,
                                           @RequestParam(value = "page", defaultValue = "0") int page,
                                           @RequestParam(value = "size", defaultValue = "3") int size) {
        return ResponseEntity.ok(videoService.listByTitle(dto, page, size));
    }

    @PutMapping("/increase-view-count/{key}")
    public ResponseEntity<?> increaseViewCount(@PathVariable("key") String key) {

        return ResponseEntity.ok(videoService.increaseViewCount(key));
    }


}

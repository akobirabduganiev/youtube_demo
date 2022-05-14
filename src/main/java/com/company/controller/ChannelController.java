package com.company.controller;

import com.company.dto.ChangeBannerDTO;
import com.company.dto.ChangeProfilePhotoDTO;
import com.company.dto.ChangeStatusDTO;
import com.company.dto.ChannelDTO;
import com.company.enums.ProfileRole;
import com.company.service.ChannelService;
import com.company.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping("/channel")
public class ChannelController {
    @Autowired
    ChannelService channelService;

    @PostMapping("/")
    public ResponseEntity<ChannelDTO> create(@RequestBody @Valid ChannelDTO dto, HttpServletRequest request) {
        Integer pId = JwtUtil.getIdFromHeader(request, ProfileRole.USER);
        return ResponseEntity.ok(channelService.create(dto, pId));
    }

    @PutMapping("/{key}")
    public ResponseEntity<ChannelDTO> update(@PathVariable("key") String key, @RequestBody @Valid ChannelDTO dto, HttpServletRequest request) {
        Integer pId = JwtUtil.getIdFromHeader(request, ProfileRole.USER);

        return ResponseEntity.ok(channelService.update(dto, key, pId));
    }

    @PutMapping("/update-photo")
    public ResponseEntity<?> updatePhoto(@RequestBody @Valid ChangeProfilePhotoDTO dto,
                                         HttpServletRequest request) {
        Integer pId = JwtUtil.getIdFromHeader(request, ProfileRole.USER);

        return ResponseEntity.ok(channelService.updateImage(dto, pId));
    }

    @PutMapping("/update-banner")
    public ResponseEntity<?> updateBanner(@RequestBody @Valid ChangeBannerDTO dto,
                                          HttpServletRequest request) {
        Integer pId = JwtUtil.getIdFromHeader(request, ProfileRole.USER);

        return ResponseEntity.ok(channelService.updateBanner(dto, pId));
    }

    @PutMapping("/change-status")
    public ResponseEntity<?> changeStatus(@RequestBody @Valid ChangeStatusDTO dto,
                                          HttpServletRequest request) {
        Integer pId = JwtUtil.getIdFromHeader(request, ProfileRole.USER);

        return ResponseEntity.ok(channelService.updateStatus(dto, pId));
    }

    @GetMapping("/adm/list")
    public ResponseEntity<?> list(@RequestParam(value = "page", defaultValue = "0") int page,
                                  @RequestParam(value = "size", defaultValue = "3") int size,
                                  HttpServletRequest request) {
        JwtUtil.getIdFromHeader(request, ProfileRole.ADMIN);
        return ResponseEntity.ok(channelService.paginationList(page, size));
    }


    @GetMapping("/channel-list")
    public ResponseEntity<?> list(HttpServletRequest request) {
        Integer pId = JwtUtil.getIdFromHeader(request, ProfileRole.USER);

        return ResponseEntity.ok(channelService.channelList(pId));
    }
}

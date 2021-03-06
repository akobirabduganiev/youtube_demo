package com.company.controller;

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
}

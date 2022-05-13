package com.company.controller;

import com.company.dto.TagDTO;
import com.company.enums.ProfileRole;
import com.company.service.TagService;
import com.company.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping("/tag")
public class TagController {
    @Autowired
    TagService tagService;

    @PostMapping("/")
    public ResponseEntity<TagDTO> create(@RequestBody @Valid TagDTO dto) {
        return ResponseEntity.ok(tagService.create(dto));
    }

    @GetMapping("/adm/{id}")
    public ResponseEntity<TagDTO> getById(@PathVariable("id") Integer id, HttpServletRequest request) {
        JwtUtil.getIdFromHeader(request, ProfileRole.ADMIN);
        return ResponseEntity.ok(tagService.getById(id));
    }

    @GetMapping("/list")
    public ResponseEntity<?> list(@RequestParam(value = "page", defaultValue = "0") int page,
                                  @RequestParam(value = "size", defaultValue = "3") int size) {
        return ResponseEntity.ok(tagService.paginationList(page, size));
    }

    @PutMapping("/adm/{id}")
    public ResponseEntity<TagDTO> update(@PathVariable("id") Integer id, @RequestBody @Valid TagDTO dto, HttpServletRequest request) {
        JwtUtil.getIdFromHeader(request, ProfileRole.ADMIN);
        return ResponseEntity.ok(tagService.update(dto, id));
    }

    @DeleteMapping("/adm/{id}")
    public ResponseEntity<?> deleteById(@PathVariable("id") Integer id, HttpServletRequest request) {
        JwtUtil.getIdFromHeader(request, ProfileRole.ADMIN);
        return ResponseEntity.ok(tagService.deleteById(id));
    }
}

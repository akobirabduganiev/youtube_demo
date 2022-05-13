package com.company.controller;

import com.company.dto.CategoryDTO;
import com.company.enums.ProfileRole;
import com.company.service.CategoryService;
import com.company.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    CategoryService categoryService;

    @PostMapping("/adm")
    public ResponseEntity<CategoryDTO> create(@RequestBody @Valid CategoryDTO dto,
                                              HttpServletRequest request) {
        JwtUtil.getIdFromHeader(request, ProfileRole.ADMIN);
        return ResponseEntity.ok(categoryService.create(dto));
    }

    @GetMapping("/adm/{name}")
    public ResponseEntity<CategoryDTO> getByName(@PathVariable("name") String name,
                                                 HttpServletRequest request) {
        JwtUtil.getIdFromHeader(request, ProfileRole.ADMIN);
        return ResponseEntity.ok(categoryService.getByName(name));
    }

    @GetMapping("/adm/list")
    public ResponseEntity<?> list(@RequestParam(value = "page", defaultValue = "0") int page,
                                  @RequestParam(value = "size", defaultValue = "3") int size,
                                  HttpServletRequest request) {
        JwtUtil.getIdFromHeader(request, ProfileRole.ADMIN);
        return ResponseEntity.ok(categoryService.paginationList(page, size));
    }
    @PutMapping("/adm/{id}")
    public ResponseEntity<CategoryDTO> update(@PathVariable("id") Integer id,
                                              @RequestBody @Valid CategoryDTO dto,
                                              HttpServletRequest request) {
        JwtUtil.getIdFromHeader(request, ProfileRole.ADMIN);
        return ResponseEntity.ok(categoryService.update(dto, id));
    }

    @DeleteMapping("/adm/{id}")
    public ResponseEntity<?> deleteById(@PathVariable("id") Integer id,
                HttpServletRequest request) {
            JwtUtil.getIdFromHeader(request, ProfileRole.ADMIN);
            return ResponseEntity.ok(categoryService.deleteById(id));
    }
}

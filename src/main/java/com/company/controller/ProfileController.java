package com.company.controller;

import com.company.dto.ChangeProfileDetailDTO;
import com.company.dto.ChangeProfileImageDTO;
import com.company.dto.ProfileDTO;
import com.company.enums.ProfileRole;
import com.company.service.ProfileService;
import com.company.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping("/profile")
public class ProfileController {

    @Autowired
    private ProfileService profileService;

    @PostMapping("/adm")
    public ResponseEntity<?> createProfile(@RequestBody @Valid ProfileDTO dto, HttpServletRequest request) {
        Integer pId = JwtUtil.getIdFromHeader(request);
        return ResponseEntity.ok(profileService.create(dto, pId));
    }

    @GetMapping("/adm/{id}")
    public ResponseEntity<?> getProfileById(@PathVariable("id") Integer id, HttpServletRequest request) {
        Integer pId = JwtUtil.getIdFromHeader(request);
        return ResponseEntity.ok(profileService.getById(id, pId));
    }

    @PutMapping("/adm/{id}")
    public ResponseEntity<?> update(@PathVariable("id") Integer id, @RequestBody @Valid ProfileDTO dto, HttpServletRequest request) {
        Integer pId = JwtUtil.getIdFromHeader(request);
        return ResponseEntity.ok(profileService.update(id, dto, pId));
    }

    @DeleteMapping("/adm/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Integer id, HttpServletRequest request) {
        Integer pId = JwtUtil.getIdFromHeader(request);
        return ResponseEntity.ok(profileService.delete(id, pId));
    }


    @PutMapping("/image")
    public ResponseEntity<?> updateImage(@RequestBody ChangeProfileImageDTO dto,
                                         HttpServletRequest request) {
        Integer pId = JwtUtil.getIdFromHeader(request);
        try {
            return ResponseEntity.ok(profileService.updateImage(dto, pId));
        } catch (DataIntegrityViolationException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Attach not found");
        }


    }

    @PutMapping("/update-detail")
    public ResponseEntity<?> updateDetail(@RequestBody ChangeProfileDetailDTO dto,
                                          HttpServletRequest request){
        Integer id = JwtUtil.getIdFromHeader(request, ProfileRole.USER);

        return ResponseEntity.ok(profileService.updateProfileDetail(dto, id));
    }


}

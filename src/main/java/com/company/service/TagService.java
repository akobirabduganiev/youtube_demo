package com.company.service;

import com.company.dto.TagDTO;
import com.company.entity.TagEntity;
import com.company.exceptions.ItemAlreadyExistsException;
import com.company.exceptions.ItemNotFoundException;
import com.company.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TagService {
    @Autowired
    TagRepository tagRepository;

    public TagDTO create(TagDTO dto) {
        Optional<TagEntity> optional = tagRepository.findByName(dto.getName());

        if (optional.isPresent()) throw new ItemAlreadyExistsException("Tag already Exists!");

        TagEntity entity = new TagEntity();
        entity.setName(dto.getName());
        tagRepository.save(entity);

        dto.setId(entity.getId());
        dto.setCreatedDate(entity.getCreatedDate());

        return dto;
    }

    public TagDTO getById(Integer id){
        TagEntity entity = tagRepository.findById(id)
                .orElseThrow(() -> new ItemNotFoundException("Tag Not found!"));
        return toDTO(entity);
    }

    public List<TagDTO> paginationList(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdDate"));

        return tagRepository.findAll(pageable).stream().map(this::toDTO).collect(Collectors.toList());
    }

    public TagDTO update(TagDTO dto, Integer id){
        TagEntity entity = tagRepository.findById(id)
                .orElseThrow(() -> new ItemNotFoundException("Tag Not found!"));
        entity.setName(dto.getName());
        entity.setLastModifiedDate(dto.getLastModifiedDate());

        tagRepository.save(entity);

        return toDTO(entity);
    }

    public String deleteById(Integer id){
        TagEntity entity = tagRepository.findById(id)
                .orElseThrow(() -> new ItemNotFoundException("Tag Not found!"));

        tagRepository.delete(entity);

        return "Delete Successfully!";
    }

    private TagDTO toDTO(TagEntity entity) {
        var dto = new TagDTO();

        dto.setName(entity.getName());
        dto.setCreatedDate(entity.getCreatedDate());
        dto.setId(entity.getId());
        dto.setLastModifiedDate(entity.getLastModifiedDate());

        return dto;
    }
}

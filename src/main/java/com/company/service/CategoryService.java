package com.company.service;

import com.company.dto.CategoryDTO;
import com.company.entity.CategoryEntity;
import com.company.exceptions.ItemAlreadyExistsException;
import com.company.exceptions.ItemNotFoundException;
import com.company.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CategoryService {
    @Autowired
    CategoryRepository categoryRepository;

    public CategoryDTO create(CategoryDTO dto) {
        Optional<CategoryEntity> optional = categoryRepository.findByName(dto.getName());

        if (optional.isPresent()) throw new ItemAlreadyExistsException("Category already created!");
        CategoryEntity entity = new CategoryEntity();

        entity.setName(dto.getName());
        categoryRepository.save(entity);

        dto.setCreatedDate(entity.getCreatedDate());
        dto.setId(entity.getId());

        return dto;
    }

    public CategoryDTO getByName(String name) {
        var entity = categoryRepository.findByName(name).orElseThrow(() -> new ItemNotFoundException("Category not found!"));
        return toDTO(entity);
    }

    public List<CategoryDTO> paginationList(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdDate"));

        return categoryRepository.findAll(pageable).stream().map(this::toDTO).collect(Collectors.toList());
    }

    public CategoryDTO update(CategoryDTO dto, Integer id) {
        var entity = categoryRepository.findById(id)
                .orElseThrow(() -> new ItemNotFoundException("Category not found!"));

        entity.setName(dto.getName());

        categoryRepository.findByName(entity.getName())
                .orElseThrow(() -> new ItemAlreadyExistsException("Category already exists!"));

        entity.setLastModifiedDate(LocalDateTime.now());
        categoryRepository.save(entity);

        dto.setCreatedDate(entity.getCreatedDate());
        dto.setLastModifiedDate(entity.getLastModifiedDate());
        dto.setId(entity.getId());

        return dto;
    }

    public String deleteById(Integer id) {
        var entity = categoryRepository.findById(id)
                .orElseThrow(() -> new ItemNotFoundException("Category not found!"));

        categoryRepository.delete(entity);

        return "Delete Successfully!";
    }

    private CategoryDTO toDTO(CategoryEntity entity) {
        var dto = new CategoryDTO();

        dto.setName(entity.getName());
        dto.setCreatedDate(entity.getCreatedDate());
        dto.setId(entity.getId());
        dto.setLastModifiedDate(entity.getLastModifiedDate());

        return dto;
    }


}

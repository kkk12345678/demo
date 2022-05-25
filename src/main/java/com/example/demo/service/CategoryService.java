package com.example.demo.service;

import com.example.demo.converter.CategoryConverter;
import com.example.demo.dto.CategoryDto;
import com.example.demo.entity.Category;
import com.example.demo.exception.CategoryNotFoundException;
import com.example.demo.exception.ValidationException;
import com.example.demo.repository.CategoryRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;

@AllArgsConstructor
@Service
public class CategoryService implements DtoService<CategoryDto> {

    private final CategoryRepository categoryRepository;
    private final CategoryConverter categoryConverter;


    @Override
    public CategoryDto save(CategoryDto categoryDto) throws ValidationException {
        validateCategory(categoryDto);
        Category savedCategory = categoryRepository.save(categoryConverter.fromCategoryDtoToCategory(categoryDto));
        return categoryConverter.fromCategoryToCategoryDto(savedCategory);
    }

    @Override
    public void delete(Integer id) throws CategoryNotFoundException {
        categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException(id));
        categoryRepository.deleteById(id);
    }

    @Override
    public CategoryDto findByName(String name) {
        Category category = categoryRepository.findByName(name);
        if (category != null) {
            return categoryConverter.fromCategoryToCategoryDto(category);
        }
        return null;
    }

    @Override
    public List<CategoryDto> findAll() {
        return categoryRepository
                .findAll()
                .stream()
                .map(categoryConverter::fromCategoryToCategoryDto)
                .collect(Collectors.toList());
    }

    @Override
    public CategoryDto findById(Integer id) throws CategoryNotFoundException {
        return categoryConverter.fromCategoryToCategoryDto(categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException(id)));
    }

    private void validateCategory(CategoryDto categoryDto) throws ValidationException {
        if (isNull(categoryDto)) {
            throw new ValidationException("Object Category is null");
        }
        String name = categoryDto.getName();
        if (isNull(name) || name.isEmpty()) {
            throw new ValidationException("Name is empty");
        }
        Category category = categoryRepository.findByName(name);
        if (!isNull(category)) {
            throw new ValidationException("Category with name: '" + name + "' already exists");
        }
    }

}

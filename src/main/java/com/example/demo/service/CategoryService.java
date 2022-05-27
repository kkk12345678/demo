package com.example.demo.service;

import com.example.demo.converter.CategoryConverter;
import com.example.demo.dto.CategoryDto;
import com.example.demo.exception.ForeignKeyConstraintSqlException;
import com.example.demo.exception.NotFoundException;
import com.example.demo.model.Category;
import com.example.demo.exception.ValidationException;
import com.example.demo.repository.CategoryRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;

@AllArgsConstructor
@Service
public class CategoryService implements CatalogueService<CategoryDto> {

    private final CategoryRepository categoryRepository;
    private final CategoryRepository titleRepository;
    private final CategoryConverter categoryConverter;


    @Override
    public CategoryDto save(CategoryDto categoryDto) throws ValidationException {
        validateCategory(categoryDto);
        Category savedCategory = categoryRepository.save(categoryConverter.fromCategoryDtoToCategory(categoryDto));
        return categoryConverter.fromCategoryToCategoryDto(savedCategory);
    }

    @Override
    public void delete(Integer id) throws NotFoundException, ForeignKeyConstraintSqlException {
        categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Category with id: " + id + " does not exist"));

        try {
            categoryRepository.deleteById(id);
        }
        catch (Exception e) {
            throw new ForeignKeyConstraintSqlException("Unable to delete category with id:" + id + ". There are titles in this category.");
        }
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
    public CategoryDto findById(Integer id) throws NotFoundException {
        return categoryConverter.fromCategoryToCategoryDto(categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Category with id: " + id + " does not exist")));
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

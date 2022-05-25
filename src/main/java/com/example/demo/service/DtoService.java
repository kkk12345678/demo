package com.example.demo.service;

import com.example.demo.dto.CategoryDto;
import com.example.demo.exception.ValidationException;

import java.util.List;
import java.util.Optional;

public interface DtoService<T> {
    T save(T dto) throws ValidationException;
    void delete(Integer id);

    CategoryDto findByName(String name);

    List<T> findAll();

    T findById(Integer id);
}

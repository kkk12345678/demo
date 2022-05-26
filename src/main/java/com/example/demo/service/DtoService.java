package com.example.demo.service;

import com.example.demo.dto.Dto;
import com.example.demo.exception.ForeignKeyConstraintSqlException;
import com.example.demo.exception.NotFoundException;
import com.example.demo.exception.ValidationException;

import java.util.List;

public interface DtoService<T> {
    T save(T dto) throws ValidationException;
    void delete(Integer id) throws NotFoundException, ForeignKeyConstraintSqlException;
    Dto findByName(String name);
    List<T> findAll();
    T findById(Integer id) throws NotFoundException;
}

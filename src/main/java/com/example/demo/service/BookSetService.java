package com.example.demo.service;

import com.example.demo.converter.BookSetConverter;
import com.example.demo.dto.BookSetDto;
import com.example.demo.exception.NotFoundException;
import com.example.demo.exception.ValidationException;
import com.example.demo.model.BookSet;
import com.example.demo.repository.BookSetRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;

@AllArgsConstructor
@Service
public class BookSetService implements CatalogueService<BookSetDto> {

    private final BookSetRepository bookSetRepository;
    private final BookSetConverter bookSetConverter;


    @Override
    public BookSetDto save(BookSetDto bookSetDto) throws ValidationException {
        validateBookSet(bookSetDto);
        BookSet savedBookSet = bookSetRepository.save(bookSetConverter.fromBookSetDtoToBookSet(bookSetDto));
        return bookSetConverter.fromBookSetToBookSetDto(savedBookSet);
    }

    @Override
    public void delete(Integer id) throws NotFoundException {
        bookSetRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("BookSet with id: " + id + " does not exist"));
        bookSetRepository.deleteById(id);
    }

    @Override
    public BookSetDto findByName(String name) {
        BookSet bookSet = bookSetRepository.findByName(name);
        if (bookSet != null) {
            return bookSetConverter.fromBookSetToBookSetDto(bookSet);
        }
        return null;
    }

    @Override
    public List<BookSetDto> findAll() {
        return bookSetRepository
                .findAll()
                .stream()
                .map(bookSetConverter::fromBookSetToBookSetDto)
                .collect(Collectors.toList());
    }

    @Override
    public BookSetDto findById(Integer id) throws NotFoundException {
        return bookSetConverter.fromBookSetToBookSetDto(bookSetRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("BookSet with id: " + id + " does not exist")));
    }

    private void validateBookSet(BookSetDto bookSetDto) throws ValidationException {
        if (isNull(bookSetDto)) {
            throw new ValidationException("Object BookSet is null");
        }
        String name = bookSetDto.getName();
        if (isNull(name) || name.isEmpty()) {
            throw new ValidationException("Name is empty");
        }
        BookSet bookSet = bookSetRepository.findByName(name);
        if (!isNull(bookSet)) {
            throw new ValidationException("BookSet with name: '" + name + "' already exists");
        }
    }

}

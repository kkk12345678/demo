package com.example.demo.service;

import com.example.demo.converter.TitleConverter;
import com.example.demo.dto.TitleDto;
import com.example.demo.exception.NotFoundException;
import com.example.demo.model.Title;
import com.example.demo.exception.ValidationException;
import com.example.demo.repository.TitleRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;

@AllArgsConstructor
@Service
public class TitleService implements DtoService<TitleDto> {

    private final TitleRepository titleRepository;
    private final TitleConverter titleConverter;


    @Override
    public TitleDto save(TitleDto titleDto) throws ValidationException {
        validateTitle(titleDto);
        Title savedTitle = titleRepository.save(titleConverter.fromTitleDtoToTitle(titleDto));
        return titleConverter.fromTitleToTitleDto(savedTitle);
    }

    @Override
    public void delete(Integer id) throws NotFoundException {
        titleRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Title with id: " + id + " does not exist"));
        titleRepository.deleteById(id);
    }

    @Override
    public TitleDto findByName(String name) {
        Title title = titleRepository.findByName(name);
        if (title != null) {
            return titleConverter.fromTitleToTitleDto(title);
        }
        return null;
    }

    @Override
    public List<TitleDto> findAll() {
        return titleRepository
                .findAll()
                .stream()
                .map(titleConverter::fromTitleToTitleDto)
                .collect(Collectors.toList());
    }

    @Override
    public TitleDto findById(Integer id) throws NotFoundException {
        return titleConverter.fromTitleToTitleDto(titleRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Title with id: " + id + " does not exist")));
    }

    private void validateTitle(TitleDto titleDto) throws ValidationException {
        if (isNull(titleDto)) {
            throw new ValidationException("Object Title is null");
        }
        String name = titleDto.getName();
        if (isNull(name) || name.isEmpty()) {
            throw new ValidationException("Name is empty");
        }
        Title title = titleRepository.findByName(name);
        if (!isNull(title)) {
            throw new ValidationException("Title with name: '" + name + "' already exists");
        }
    }

}

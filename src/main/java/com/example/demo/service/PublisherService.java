package com.example.demo.service;

import com.example.demo.converter.PublisherConverter;
import com.example.demo.dto.PublisherDto;
import com.example.demo.entity.Publisher;
import com.example.demo.exception.PublisherNotFoundException;
import com.example.demo.exception.ValidationException;
import com.example.demo.repository.PublisherRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;

@AllArgsConstructor
@Service
public class PublisherService implements DtoService<PublisherDto> {
    private final PublisherRepository publisherRepository;
    private final PublisherConverter publisherConverter;

    @Override
    public PublisherDto save(PublisherDto publisherDto) throws ValidationException {
        validatePublisher(publisherDto);
        Publisher savedPublisher = publisherRepository.save(publisherConverter.fromPublisherDtoToPublisher(publisherDto));
        return publisherConverter.fromPublisherToPublisherDto(savedPublisher);
    }

    @Override
    public void delete(Integer id) throws PublisherNotFoundException {
        publisherRepository.findById(id)
                .orElseThrow(() -> new PublisherNotFoundException(id));
        publisherRepository.deleteById(id);
    }

    @Override
    public PublisherDto findByName(String name) {
        Publisher publisher = publisherRepository.findByName(name);
        if (publisher != null) {
            return publisherConverter.fromPublisherToPublisherDto(publisher);
        }
        return null;
    }

    @Override
    public List<PublisherDto> findAll() {
        return publisherRepository
                .findAll()
                .stream()
                .map(publisherConverter::fromPublisherToPublisherDto)
                .collect(Collectors.toList());
    }

    @Override
    public PublisherDto findById(Integer id) throws PublisherNotFoundException {
        return publisherConverter.fromPublisherToPublisherDto(publisherRepository.findById(id)
                .orElseThrow(() -> new PublisherNotFoundException(id)));
    }

    private void validatePublisher(PublisherDto publisherDto) throws ValidationException {
        if (isNull(publisherDto)) {
            throw new ValidationException("Object Publisher is null");
        }
        String name = publisherDto.getName();
        if (isNull(name) || name.isEmpty()) {
            throw new ValidationException("Name is empty");
        }
        Publisher publisher = publisherRepository.findByName(name);
        if (!isNull(publisher)) {
            throw new ValidationException("Publisher with name: '" + name + "' already exists");
        }
    }
}

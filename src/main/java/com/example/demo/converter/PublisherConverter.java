package com.example.demo.converter;

import com.example.demo.dto.PublisherDto;
import com.example.demo.model.Publisher;
import org.springframework.stereotype.Component;

@Component
public class PublisherConverter {
    public PublisherDto fromPublisherToPublisherDto(Publisher publisher) {
        return PublisherDto.builder()
                .id(publisher.getId())
                .name(publisher.getName())
                .description(publisher.getDescription())
                .img(publisher.getImg())
                .build();
    }

    public Publisher fromPublisherDtoToPublisher(PublisherDto publisherDto) {
        Publisher publisher = new Publisher();
        publisher.setId(publisherDto.getId());
        publisher.setName(publisherDto.getName());
        publisher.setDescription(publisherDto.getDescription());
        publisher.setImg(publisherDto.getImg());
        return publisher;
    }
}

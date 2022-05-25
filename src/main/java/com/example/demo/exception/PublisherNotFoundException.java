package com.example.demo.exception;

public class PublisherNotFoundException extends RuntimeException {
    public PublisherNotFoundException(Integer id) {
        super("Could not find publisher with id: " + id);
    }
}

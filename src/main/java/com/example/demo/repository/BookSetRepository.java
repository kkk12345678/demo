package com.example.demo.repository;

import com.example.demo.model.BookSet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookSetRepository extends JpaRepository<BookSet, Integer> {
    BookSet findByName(String name);
}

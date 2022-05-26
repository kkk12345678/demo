package com.example.demo.repository;

import com.example.demo.model.Title;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TitleRepository extends JpaRepository<Title, Integer> {
    Title findByName(String name);
}

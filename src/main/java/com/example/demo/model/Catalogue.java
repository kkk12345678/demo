package com.example.demo.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;


@Data
@MappedSuperclass
public abstract class Catalogue {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true)
    protected Integer id;

    @Column(name = "name", unique = true, nullable = false)
    protected String name;

    @Lob
    @Column(name="description", length=4096)
    protected String description;

    @Column(name="img")
    protected String img;
}

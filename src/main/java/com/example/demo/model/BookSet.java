package com.example.demo.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "booksets")
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class BookSet extends Catalogue {
    @Column(nullable = false)
    private Integer price15;

    @Column
    private Integer price30;

    @ManyToOne(optional=false)
    private Title title;
}

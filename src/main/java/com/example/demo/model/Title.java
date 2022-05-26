package com.example.demo.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "titles")
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Title extends Catalogue {
    @ManyToOne(optional=false)
    Category category;
    @ManyToOne(optional=false)
    Publisher publisher;
}

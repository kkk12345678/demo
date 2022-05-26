package com.example.demo.model;


import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "categories")
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Category extends Catalogue {
}

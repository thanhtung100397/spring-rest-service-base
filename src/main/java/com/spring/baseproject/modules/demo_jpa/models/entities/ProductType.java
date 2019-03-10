package com.spring.baseproject.modules.demo_jpa.models.entities;

import com.spring.baseproject.modules.demo_jpa.models.dtos.NewProductTypeDto;

import javax.persistence.*;

@Entity
@Table(name = "product_type")
public class ProductType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)// auto increment field
    @Column(name = "id")
    private int id;
    @Column(name = "name", nullable = false)// not null field
    private String name;

    // always create an empty constructor
    public ProductType() {
    }

    public ProductType(NewProductTypeDto newProductTypeDto) {
        update(newProductTypeDto);
    }

    public void update(NewProductTypeDto newProductTypeDto) {
        this.name = newProductTypeDto.getName();
    }

    // always create full getter and setter
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

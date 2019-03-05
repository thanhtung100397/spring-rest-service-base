package com.spring.baseproject.modules.demo_jpa.models.entities;

import javax.persistence.*;

@Entity
@Table(name = "product_model")
public class ProductModel {
    @Id// primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY)// auto increment field
    private int id;
    private String name;

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

package com.spring.baseproject.modules.demo_jpa.models.entities;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "product")
public class Product {
    @Id// primary key
    @GenericGenerator(name = "uuid", strategy = "uuid2")// using uuid for primary key
    @GeneratedValue(generator = "uuid")// using uuid for primary key
    @Column(name = "id", length = 36)
    private String id;
    @Column(nullable = false)
    private String name;
    @Enumerated(EnumType.STRING)// using enum name (string) as value of field
    private ProductSize productSize;
    private Date createdDate;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "productTypeID")
    private ProductType productType;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ProductSize getProductSize() {
        return productSize;
    }

    public void setProductSize(ProductSize productSize) {
        this.productSize = productSize;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public ProductType getProductType() {
        return productType;
    }

    public void setProductType(ProductType productType) {
        this.productType = productType;
    }
}

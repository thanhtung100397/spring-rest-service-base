package com.spring.baseproject.modules.demo_jpa.models.entities;

import com.spring.baseproject.base.json_converter.JsonListConverter;
import com.spring.baseproject.modules.demo_jpa.models.dtos.NewProductDto;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "product")
public class Product {
    @Id// primary key field annotation
    @GenericGenerator(name = "uuid", strategy = "uuid2")// using uuid generator for primary key field
    @GeneratedValue(generator = "uuid")// using uuid generator for primary key field
    @Column(name = "id", length = 36)// uuid has 36 character length
    private String id;
    @Column(name = "name", nullable = false)// not null field
    private String name;
    @Enumerated(EnumType.STRING)// using enum name (string) as value of field
    @Column(name = "product_size", columnDefinition = "TEXT")
    private ProductSize productSize;
    @Column(name = "created_date")
    private Date createdDate;
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Convert(converter = JsonListConverter.class)// json list converter
    private List<String> tags;

    @OneToOne(
            fetch = FetchType.LAZY,// always using LAZY fetching strategy
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @JoinColumn(name = "product_type_id")
    private ProductType productType;

    // always create an empty constructor
    public Product() {
    }

    public Product(ProductType productType, NewProductDto newProductDto) {
        update(productType, newProductDto);
        this.createdDate = new Date();
    }

    public void update(ProductType productType, NewProductDto newProductDto) {
        this.productType = productType;
        this.name = newProductDto.getName();
        this.productSize = newProductDto.getProductSize();
        this.description = newProductDto.getDescription();
        this.tags = newProductDto.getTags();
    }

    // always create full getter and setter
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public ProductType getProductType() {
        return productType;
    }

    public void setProductType(ProductType productType) {
        this.productType = productType;
    }
}

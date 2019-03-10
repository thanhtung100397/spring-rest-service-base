package com.spring.baseproject.modules.demo_jpa.repositories;

import com.spring.baseproject.modules.demo_jpa.models.dtos.ProductDto;
import com.spring.baseproject.modules.demo_jpa.models.dtos.ProductPreviewDto;
import com.spring.baseproject.modules.demo_jpa.models.entities.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

public interface ProductRepository extends JpaRepository<Product, String> {

    @Query("select new com.spring.baseproject" +
            ".modules.demo_jpa.models.dtos.ProductPreviewDto(p.id, p.name, p.createdDate, p.tags, pt.id, pt.name) " +
            "from Product p " +
            "left join p.productType pt")
    Page<ProductPreviewDto> getPageProductPreviewDtos(Pageable pageable);

    @Query("select new com.spring.baseproject" +
            ".modules.demo_jpa.models.dtos.ProductDto(p.id, p.name, p.createdDate, p.tags, pt.id, pt.name, p.productSize, p.description) " +
            "from Product p " +
            "left join p.productType pt " +
            "where p.id = ?1")
    ProductDto getProductDto(String productID);

    Product findFirstById(String id);

    @Modifying
    @Transactional
    void deleteById(String id);

    @Modifying
    @Transactional
    void deleteAllByIdIn(Set<String> ids);
}

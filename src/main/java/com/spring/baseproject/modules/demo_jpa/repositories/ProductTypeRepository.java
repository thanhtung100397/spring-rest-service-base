package com.spring.baseproject.modules.demo_jpa.repositories;

import com.spring.baseproject.modules.demo_jpa.models.dtos.ProductTypeDto;
import com.spring.baseproject.modules.demo_jpa.models.entities.ProductType;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

public interface ProductTypeRepository extends JpaRepository<ProductType, Integer> {
    ProductType findFirstById(Integer id);

    @Query("select new com.spring.baseproject.modules.demo_jpa.models.dtos.ProductTypeDto(pt.id, pt.name) " +
            "from ProductType pt")
    List<ProductTypeDto> getListProductTypeDtos(Sort sort);

    @Query("select new com.spring.baseproject.modules.demo_jpa.models.dtos.ProductTypeDto(pt.id, pt.name) " +
            "from ProductType pt " +
            "where pt.id = ?1")
    ProductTypeDto getProductTypeDto(Integer id);

    @Modifying
    @Transactional
    void deleteById(Integer id);

    @Modifying
    @Transactional
    void deleteAllByIdIn(Set<Integer> ids);
}

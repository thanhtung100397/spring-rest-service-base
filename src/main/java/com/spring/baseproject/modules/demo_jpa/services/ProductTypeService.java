package com.spring.baseproject.modules.demo_jpa.services;

import com.spring.baseproject.constants.ResponseValue;
import com.spring.baseproject.exceptions.ResponseException;
import com.spring.baseproject.modules.demo_jpa.models.dtos.*;
import com.spring.baseproject.modules.demo_jpa.models.entities.ProductType;
import com.spring.baseproject.modules.demo_jpa.repositories.ProductTypeRepository;
import com.spring.baseproject.utils.jpa.SortAndPageFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class ProductTypeService {
    @Autowired
    private ProductTypeRepository productTypeRepository;

    public List<ProductTypeDto> getListProductTypeDtos(List<String> sortBy, List<String> sortType) {
        Sort sort = SortAndPageFactory.createSort(sortBy, sortType);
        return productTypeRepository.getListProductTypeDtos(sort);
    }

    public ProductTypeDto getProductTypeDto(Integer productTypeID) throws ResponseException {
        ProductTypeDto productTypeDto = productTypeRepository.getProductTypeDto(productTypeID);
        if (productTypeDto == null) {
            throw new ResponseException(ResponseValue.PRODUCT_TYPE_NOT_FOUND);
        }
        return productTypeDto;
    }

    public void createNewProductType(NewProductTypeDto newProductTypeDto) {
        ProductType productType = new ProductType(newProductTypeDto);
        productTypeRepository.save(productType);
    }

    public void updateProductType(Integer productTypeID, NewProductTypeDto newProductTypeDto) throws ResponseException {
        ProductType productType = productTypeRepository.findFirstById(productTypeID);
        if (productType == null) {
            throw new ResponseException(ResponseValue.PRODUCT_TYPE_NOT_FOUND);
        }
        productType.update(newProductTypeDto);
        productTypeRepository.save(productType);
    }

    public void deleteProductType(Integer productTypeID) throws ResponseException {
        try {
            productTypeRepository.deleteById(productTypeID);
        } catch (EmptyResultDataAccessException e) {
            throw new ResponseException(ResponseValue.PRODUCT_TYPE_NOT_FOUND);
        }
    }

    public void deleteListProductTypes(Set<Integer> productTypeIDs) {
        productTypeRepository.deleteAllByIdIn(productTypeIDs);
    }
}

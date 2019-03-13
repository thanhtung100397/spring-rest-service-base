package com.spring.baseproject.modules.demo_jpa.services;

import com.spring.baseproject.base.models.BaseResponse;
import com.spring.baseproject.constants.ResponseValue;
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

    public BaseResponse getListProductTypeDtos(List<String> sortBy, List<String> sortType) {
        Sort sort = SortAndPageFactory.createSort(sortBy, sortType);
        List<ProductTypeDto> productTypeDtos = productTypeRepository.getListProductTypeDtos(sort);
        return new BaseResponse(ResponseValue.SUCCESS, productTypeDtos);
    }

    public BaseResponse getProductTypeDto(Integer productTypeID) {
        ProductTypeDto productTypeDto = productTypeRepository.getProductTypeDto(productTypeID);
        if (productTypeDto == null) {
            return new BaseResponse(ResponseValue.PRODUCT_TYPE_NOT_FOUND);
        }
        return new BaseResponse(ResponseValue.SUCCESS, productTypeDto);
    }

    public BaseResponse createNewProductType(NewProductTypeDto newProductTypeDto) {
        ProductType productType = new ProductType(newProductTypeDto);
        productTypeRepository.save(productType);
        return new BaseResponse(ResponseValue.SUCCESS);
    }

    public BaseResponse updateProductType(Integer productTypeID, NewProductTypeDto newProductTypeDto) {
        ProductType productType = productTypeRepository.findFirstById(productTypeID);
        if (productType == null) {
            return new BaseResponse(ResponseValue.PRODUCT_TYPE_NOT_FOUND);
        }
        productType.update(newProductTypeDto);
        productTypeRepository.save(productType);
        return new BaseResponse(ResponseValue.SUCCESS);
    }

    public BaseResponse deleteProductType(Integer productTypeID) {
        try {
            productTypeRepository.deleteById(productTypeID);
            return new BaseResponse(ResponseValue.SUCCESS);
        } catch (EmptyResultDataAccessException e) {
            return new BaseResponse(ResponseValue.PRODUCT_TYPE_NOT_FOUND);
        }
    }

    public BaseResponse deleteListProductTypes(Set<Integer> productTypeIDs) {
        productTypeRepository.deleteAllByIdIn(productTypeIDs);
        return new BaseResponse(ResponseValue.SUCCESS);
    }
}

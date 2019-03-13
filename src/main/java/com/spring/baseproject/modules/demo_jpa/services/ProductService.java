package com.spring.baseproject.modules.demo_jpa.services;

import com.spring.baseproject.base.models.BaseResponse;
import com.spring.baseproject.constants.NumberConstants;
import com.spring.baseproject.constants.ResponseValue;
import com.spring.baseproject.modules.demo_jpa.models.dtos.NewProductDto;
import com.spring.baseproject.modules.demo_jpa.models.dtos.ProductDto;
import com.spring.baseproject.modules.demo_jpa.models.dtos.ProductPreviewDto;
import com.spring.baseproject.modules.demo_jpa.models.entities.Product;
import com.spring.baseproject.modules.demo_jpa.models.entities.ProductType;
import com.spring.baseproject.modules.demo_jpa.repositories.ProductRepository;
import com.spring.baseproject.modules.demo_jpa.repositories.ProductTypeRepository;
import com.spring.baseproject.utils.jpa.SortAndPageFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ProductTypeRepository productTypeRepository;

    public BaseResponse getPageProductPreviewDtos(List<String> sortBy, List<String> sortType,
                                                  int pageIndex, int pageSize) {
        Pageable pageable = SortAndPageFactory
                .createPageable(sortBy, sortType, pageIndex, pageSize, NumberConstants.MAX_PAGE_SIZE);
        Page<ProductPreviewDto> pageProductPreviewDtos = productRepository.getPageProductPreviewDtos(pageable);
        return new BaseResponse(ResponseValue.SUCCESS, pageProductPreviewDtos);
    }

    public BaseResponse getProductDto(String productID) {
        ProductDto productDto = productRepository.getProductDto(productID);
        if (productDto == null) {
            return new BaseResponse(ResponseValue.PRODUCT_NOT_FOUND);
        }
        return new BaseResponse(ResponseValue.SUCCESS, productDto);
    }

    public BaseResponse createNewProduct(NewProductDto newProductDto) {
        ProductType productType = productTypeRepository.findFirstById(newProductDto.getProductTypeID());
        if (productType == null) {
            return new BaseResponse(ResponseValue.PRODUCT_TYPE_NOT_FOUND);
        }
        Product product = new Product(productType, newProductDto);
        productRepository.save(product);
        return new BaseResponse(ResponseValue.SUCCESS);
    }

    public BaseResponse updateProduct(String productID, NewProductDto newProductDto) {
        ProductType productType = productTypeRepository.findFirstById(newProductDto.getProductTypeID());
        if (productType == null) {
            return new BaseResponse(ResponseValue.PRODUCT_TYPE_NOT_FOUND);
        }
        Product product = productRepository.findFirstById(productID);
        if (product == null) {
            return new BaseResponse(ResponseValue.PRODUCT_NOT_FOUND);
        }
        product.update(productType, newProductDto);
        productRepository.save(product);
        return new BaseResponse(ResponseValue.SUCCESS);
    }

    public BaseResponse deleteProduct(String productID) {
        try {
            productRepository.deleteById(productID);
            return new BaseResponse(ResponseValue.SUCCESS);
        } catch (EmptyResultDataAccessException e) {
            return new BaseResponse(ResponseValue.PRODUCT_NOT_FOUND);
        }
    }

    public BaseResponse deleteListProducts(Set<String> productIDs) {
        productRepository.deleteAllByIdIn(productIDs);
        return new BaseResponse(ResponseValue.SUCCESS);
    }
}

package com.spring.baseproject.modules.demo_jpa.services;

import com.spring.baseproject.constants.NumberConstants;
import com.spring.baseproject.constants.ResponseValue;
import com.spring.baseproject.exceptions.ResponseException;
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

    public Page<ProductPreviewDto> getPageProductPreviewDtos(List<String> sortBy, List<String> sortType,
                                                             int pageIndex, int pageSize) {
        Pageable pageable = SortAndPageFactory
                .createPageable(sortBy, sortType, pageIndex, pageSize, NumberConstants.MAX_PAGE_SIZE);
        return productRepository.getPageProductPreviewDtos(pageable);
    }

    public ProductDto getProductDto(String productID) throws ResponseException {
        ProductDto productDto = productRepository.getProductDto(productID);
        if (productDto == null) {
            throw new ResponseException(ResponseValue.PRODUCT_NOT_FOUND);
        }
        return productDto;
    }

    public void createNewProduct(NewProductDto newProductDto) throws ResponseException {
        ProductType productType = productTypeRepository.findFirstById(newProductDto.getProductTypeID());
        if (productType == null) {
            throw new ResponseException(ResponseValue.PRODUCT_TYPE_NOT_FOUND);
        }
        Product product = new Product(productType, newProductDto);
        productRepository.save(product);
    }

    public void updateProduct(String productID, NewProductDto newProductDto) throws ResponseException {
        ProductType productType = productTypeRepository.findFirstById(newProductDto.getProductTypeID());
        if (productType == null) {
            throw new ResponseException(ResponseValue.PRODUCT_TYPE_NOT_FOUND);
        }
        Product product = productRepository.findFirstById(productID);
        if (product == null) {
            throw new ResponseException(ResponseValue.PRODUCT_NOT_FOUND);
        }
        product.update(productType, newProductDto);
        productRepository.save(product);
    }

    public void deleteProduct(String productID) throws ResponseException {
        try {
            productRepository.deleteById(productID);
        } catch (EmptyResultDataAccessException e) {
            throw new ResponseException(ResponseValue.PRODUCT_NOT_FOUND);
        }
    }

    public void deleteListProducts(Set<String> productIDs) {
        productRepository.deleteAllByIdIn(productIDs);
    }
}

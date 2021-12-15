package com.spring.baseproject.modules.demo_jpa.controllers;

import com.spring.baseproject.annotations.swagger.Response;
import com.spring.baseproject.annotations.swagger.Responses;
import com.spring.baseproject.constants.NumberConstants;
import com.spring.baseproject.constants.ResponseValue;
import com.spring.baseproject.constants.StringConstants;
import com.spring.baseproject.exceptions.ResponseException;
import com.spring.baseproject.modules.demo_jpa.models.dtos.NewProductDto;
import com.spring.baseproject.modules.demo_jpa.models.dtos.ProductDto;
import com.spring.baseproject.modules.demo_jpa.models.dtos.ProductPreviewDto;
import com.spring.baseproject.modules.demo_jpa.services.ProductService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/jpa-demo")
@Api(description = "Sản phẩm")
public class ProductController {
    @Autowired
    private ProductService productService;

    @ApiOperation(value = "Lấy danh sách sản phẩm", response = Iterable.class)
    @Responses(value = {
            @Response(responseValue = ResponseValue.SUCCESS)
    })
    @GetMapping("/products")
    public Page<ProductPreviewDto> getPageProductPreviews(
            @RequestParam(value = StringConstants.SORT_BY, defaultValue = "", required = false) List<String> sortBy,
            @RequestParam(value = StringConstants.SORT_TYPE, defaultValue = "", required = false) List<String> sortType,
            @RequestParam(value = StringConstants.PAGE_INDEX, defaultValue = "0") int pageIndex,
            @RequestParam(value = StringConstants.PAGE_SIZE, defaultValue = NumberConstants.MAX_PAGE_SIZE + "") int pageSize
    ) {
        return productService.getPageProductPreviewDtos(sortBy, sortType, pageIndex, pageSize);
    }

    @ApiOperation(value = "Tạo mới một sản phẩm", response = Iterable.class)
    @Responses(value = {
            @Response(responseValue = ResponseValue.SUCCESS)
    })
    @PostMapping("/products")
    public void addProduct(@RequestBody @Valid NewProductDto newProductDto) throws ResponseException {
        productService.createNewProduct(newProductDto);
    }

    @ApiOperation(value = "Xóa một danh sách sản phẩm", response = Iterable.class)
    @Responses(value = {
            @Response(responseValue = ResponseValue.SUCCESS)
    })
    @DeleteMapping("/products")
    public void deleteProduct(@RequestBody Set<String> productIDs) {
        productService.deleteListProducts(productIDs);
    }

    @ApiOperation(value = "Xem chi tiết một sản phẩm", response = Iterable.class)
    @Responses(value = {
            @Response(responseValue = ResponseValue.SUCCESS),
            @Response(responseValue = ResponseValue.PRODUCT_NOT_FOUND)
    })
    @GetMapping("/products/{pid}")
    public ProductDto getProduct(@PathVariable("pid") String productID) throws ResponseException {
        return productService.getProductDto(productID);
    }

    @ApiOperation(value = "Cập nhật thông tin sản phẩm", response = Iterable.class)
    @Responses(value = {
            @Response(responseValue = ResponseValue.SUCCESS),
            @Response(responseValue = ResponseValue.PRODUCT_NOT_FOUND)
    })
    @PutMapping("/products/{pid}")
    public void updateProduct(@PathVariable("pid") String productID,
                              @RequestBody @Valid NewProductDto newProductDto) throws ResponseException {
        productService.updateProduct(productID, newProductDto);
    }

    @ApiOperation(value = "Xóa một sản phẩm", response = Iterable.class)
    @Responses(value = {
            @Response(responseValue = ResponseValue.SUCCESS),
            @Response(responseValue = ResponseValue.PRODUCT_NOT_FOUND)
    })
    @DeleteMapping("/products/{pid}")
    public void deleteProduct(@PathVariable("pid") String productID) throws ResponseException {
        productService.deleteProduct(productID);
    }
}

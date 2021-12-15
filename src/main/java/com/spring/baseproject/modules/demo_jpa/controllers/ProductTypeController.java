package com.spring.baseproject.modules.demo_jpa.controllers;

import com.spring.baseproject.annotations.swagger.Response;
import com.spring.baseproject.annotations.swagger.Responses;
import com.spring.baseproject.constants.ResponseValue;
import com.spring.baseproject.constants.StringConstants;
import com.spring.baseproject.exceptions.ResponseException;
import com.spring.baseproject.modules.demo_jpa.models.dtos.NewProductTypeDto;
import com.spring.baseproject.modules.demo_jpa.models.dtos.ProductTypeDto;
import com.spring.baseproject.modules.demo_jpa.services.ProductTypeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/jpa-demo")
@Api(description = "Loại sản phẩm")
public class ProductTypeController {
    @Autowired
    private ProductTypeService productTypeService;

    @ApiOperation(value = "Lấy danh sách loại sản phẩm", response = Iterable.class)
    @Responses(value = {
    })
    @GetMapping("/productTypes")
    public List<ProductTypeDto> getProductTypes(
            @RequestParam(value = StringConstants.SORT_BY, defaultValue = "", required = false) List<String> sortBy,
            @RequestParam(value = StringConstants.SORT_TYPE, defaultValue = "", required = false) List<String> sortType
    ) {
        return productTypeService.getListProductTypeDtos(sortBy, sortType);
    }

    @ApiOperation(value = "Tạo mới một loại sản phẩm", response = Iterable.class)
    @Responses(value = {
    })
    @PostMapping("/productTypes")
    public void addProductType(@RequestBody @Valid NewProductTypeDto newProductTypeDto) {
        productTypeService.createNewProductType(newProductTypeDto);
    }

    @ApiOperation(value = "Xóa một danh sách loại sản phẩm", response = Iterable.class)
    @Responses(value = {
    })
    @DeleteMapping("/productTypes")
    public void deleteProductType(@RequestBody Set<Integer> productTypeIDs) {
        productTypeService.deleteListProductTypes(productTypeIDs);
    }

    @ApiOperation(value = "Xem chi tiết một loại sản phẩm", response = Iterable.class)
    @Responses(value = {
            @Response(responseValue = ResponseValue.PRODUCT_TYPE_NOT_FOUND)
    })
    @GetMapping("/productsType/{ptid}")
    public ProductTypeDto getProductType(@PathVariable("ptid") Integer productTypeID) throws ResponseException {
        return productTypeService.getProductTypeDto(productTypeID);
    }

    @ApiOperation(value = "Cập nhật thông tin loại sản phẩm", response = Iterable.class)
    @Responses(value = {
            @Response(responseValue = ResponseValue.PRODUCT_NOT_FOUND)
    })
    @PutMapping("/productTypes/{ptid}")
    public void updateProductType(@PathVariable("ptid") Integer productTypeID,
                                  @RequestBody @Valid NewProductTypeDto newProductTypeDto) throws ResponseException {
        productTypeService.updateProductType(productTypeID, newProductTypeDto);
    }

    @ApiOperation(value = "Xóa một loại sản phẩm", response = Iterable.class)
    @Responses(value = {
            @Response(responseValue = ResponseValue.PRODUCT_TYPE_NOT_FOUND)
    })
    @DeleteMapping("/productTypes/{ptid}")
    public void deleteProductType(@PathVariable("ptid") Integer productTypeID) throws ResponseException {
        productTypeService.deleteProductType(productTypeID);
    }
}

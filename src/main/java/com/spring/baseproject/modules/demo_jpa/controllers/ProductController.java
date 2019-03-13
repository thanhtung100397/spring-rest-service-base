package com.spring.baseproject.modules.demo_jpa.controllers;

import com.spring.baseproject.annotations.swagger.Response;
import com.spring.baseproject.annotations.swagger.Responses;
import com.spring.baseproject.base.controllers.BaseRESTController;
import com.spring.baseproject.base.models.BaseResponse;
import com.spring.baseproject.base.models.BaseResponseBody;
import com.spring.baseproject.constants.NumberConstants;
import com.spring.baseproject.constants.ResponseValue;
import com.spring.baseproject.constants.StringConstants;
import com.spring.baseproject.modules.demo_jpa.models.dtos.NewProductDto;
import com.spring.baseproject.modules.demo_jpa.services.ProductService;
import com.spring.baseproject.swagger.demo_jpa.product_controller.PageProductPreviewDtosSwagger;
import com.spring.baseproject.swagger.demo_jpa.product_controller.ProductDtoSwagger;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/jpa-demo")
@Api(description = "Sản phẩm")
public class ProductController extends BaseRESTController {
    @Autowired
    private ProductService productService;

    @ApiOperation(value = "Lấy danh sách sản phẩm", response = Iterable.class)
    @Responses(value = {
            @Response(responseValue = ResponseValue.SUCCESS, responseBody = PageProductPreviewDtosSwagger.class)
    })
    @GetMapping("/products")
    public BaseResponse getPageProductPreviews(@RequestParam(value = StringConstants.SORT_BY, defaultValue = "", required = false) List<String> sortBy,
                                               @RequestParam(value = StringConstants.SORT_TYPE, defaultValue = "", required = false) List<String> sortType,
                                               @RequestParam(value = StringConstants.PAGE_INDEX, defaultValue = "0") int pageIndex,
                                               @RequestParam(value = StringConstants.PAGE_SIZE, defaultValue = NumberConstants.MAX_PAGE_SIZE + "") int pageSize) {
        return productService.getPageProductPreviewDtos(sortBy, sortType, pageIndex, pageSize);
    }

    @ApiOperation(value = "Tạo mới một sản phẩm", response = Iterable.class)
    @Responses(value = {
            @Response(responseValue = ResponseValue.SUCCESS, responseBody = BaseResponseBody.class)
    })
    @PostMapping("/products")
    public BaseResponse addProduct(@RequestBody @Valid NewProductDto newProductDto) {
        return productService.createNewProduct(newProductDto);
    }

    @ApiOperation(value = "Xóa một danh sách sản phẩm", response = Iterable.class)
    @Responses(value = {
            @Response(responseValue = ResponseValue.SUCCESS, responseBody = BaseResponseBody.class)
    })
    @DeleteMapping("/products")
    public BaseResponse deleteProduct(@RequestBody Set<String> productIDs) {
        return productService.deleteListProducts(productIDs);
    }

    @ApiOperation(value = "Xem chi tiết một sản phẩm", response = Iterable.class)
    @Responses(value = {
            @Response(responseValue = ResponseValue.SUCCESS, responseBody = ProductDtoSwagger.class),
            @Response(responseValue = ResponseValue.PRODUCT_NOT_FOUND)
    })
    @GetMapping("/products/{pid}")
    public BaseResponse getProduct(@PathVariable("pid") String productID) {
        return productService.getProductDto(productID);
    }

    @ApiOperation(value = "Cập nhật thông tin sản phẩm", response = Iterable.class)
    @Responses(value = {
            @Response(responseValue = ResponseValue.SUCCESS, responseBody = BaseResponseBody.class),
            @Response(responseValue = ResponseValue.PRODUCT_NOT_FOUND)
    })
    @PutMapping("/products/{pid}")
    public BaseResponse updateProduct(@PathVariable("pid") String productID,
                                      @RequestBody @Valid NewProductDto newProductDto) {
        return productService.updateProduct(productID, newProductDto);
    }

    @ApiOperation(value = "Xóa một sản phẩm", response = Iterable.class)
    @Responses(value = {
            @Response(responseValue = ResponseValue.SUCCESS, responseBody = BaseResponseBody.class),
            @Response(responseValue = ResponseValue.PRODUCT_NOT_FOUND)
    })
    @DeleteMapping("/products/{pid}")
    public BaseResponse deleteProduct(@PathVariable("pid") String productID) {
        return productService.deleteProduct(productID);
    }
}

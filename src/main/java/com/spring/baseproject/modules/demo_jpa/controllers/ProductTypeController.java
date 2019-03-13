package com.spring.baseproject.modules.demo_jpa.controllers;

import com.spring.baseproject.annotations.swagger.Response;
import com.spring.baseproject.annotations.swagger.Responses;
import com.spring.baseproject.base.controllers.BaseRESTController;
import com.spring.baseproject.base.models.BaseResponse;
import com.spring.baseproject.base.models.BaseResponseBody;
import com.spring.baseproject.constants.ResponseValue;
import com.spring.baseproject.constants.StringConstants;
import com.spring.baseproject.modules.demo_jpa.models.dtos.NewProductDto;
import com.spring.baseproject.modules.demo_jpa.models.dtos.NewProductTypeDto;
import com.spring.baseproject.modules.demo_jpa.services.ProductTypeService;
import com.spring.baseproject.swagger.demo_jpa.product_controller.PageProductPreviewDtosSwagger;
import com.spring.baseproject.swagger.demo_jpa.product_controller.ProductDtoSwagger;
import com.spring.baseproject.swagger.demo_jpa.product_type_controller.ListProductTypeDtosSwagger;
import com.spring.baseproject.swagger.demo_jpa.product_type_controller.ProductTypeDtoSwagger;
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
public class ProductTypeController extends BaseRESTController {
    @Autowired
    private ProductTypeService productTypeService;

    @ApiOperation(value = "Lấy danh sách loại sản phẩm", response = Iterable.class)
    @Responses(value = {
            @Response(responseValue = ResponseValue.SUCCESS, responseBody = ListProductTypeDtosSwagger.class)
    })
    @GetMapping("/productTypes")
    public BaseResponse getProductTypes(@RequestParam(value = StringConstants.SORT_BY, defaultValue = "", required = false) List<String> sortBy,
                                        @RequestParam(value = StringConstants.SORT_TYPE, defaultValue = "", required = false) List<String> sortType) {
        return productTypeService.getListProductTypeDtos(sortBy, sortType);
    }

    @ApiOperation(value = "Tạo mới một loại sản phẩm", response = Iterable.class)
    @Responses(value = {
            @Response(responseValue = ResponseValue.SUCCESS, responseBody = BaseResponseBody.class)
    })
    @PostMapping("/productTypes")
    public BaseResponse addProductType(@RequestBody @Valid NewProductTypeDto newProductTypeDto) {
        return productTypeService.createNewProductType(newProductTypeDto);
    }

    @ApiOperation(value = "Xóa một danh sách loại sản phẩm", response = Iterable.class)
    @Responses(value = {
            @Response(responseValue = ResponseValue.SUCCESS, responseBody = BaseResponseBody.class)
    })
    @DeleteMapping("/productTypes")
    public BaseResponse deleteProductType(@RequestBody Set<Integer> productTypeIDs) {
        return productTypeService.deleteListProductTypes(productTypeIDs);
    }

    @ApiOperation(value = "Xem chi tiết một loại sản phẩm", response = Iterable.class)
    @Responses(value = {
            @Response(responseValue = ResponseValue.SUCCESS, responseBody = ProductTypeDtoSwagger.class),
            @Response(responseValue = ResponseValue.PRODUCT_TYPE_NOT_FOUND)
    })
    @GetMapping("/productsType/{ptid}")
    public BaseResponse getProductType(@PathVariable("ptid") Integer productTypeID) {
        return productTypeService.getProductTypeDto(productTypeID);
    }

    @ApiOperation(value = "Cập nhật thông tin loại sản phẩm", response = Iterable.class)
    @Responses(value = {
            @Response(responseValue = ResponseValue.SUCCESS, responseBody = BaseResponseBody.class),
            @Response(responseValue = ResponseValue.PRODUCT_NOT_FOUND)
    })
    @PutMapping("/productTypes/{ptid}")
    public BaseResponse updateProductType(@PathVariable("ptid") Integer productTypeID,
                                          @RequestBody @Valid NewProductTypeDto newProductTypeDto) {
        return productTypeService.updateProductType(productTypeID, newProductTypeDto);
    }

    @ApiOperation(value = "Xóa một loại sản phẩm", response = Iterable.class)
    @Responses(value = {
            @Response(responseValue = ResponseValue.SUCCESS, responseBody = BaseResponseBody.class),
            @Response(responseValue = ResponseValue.PRODUCT_TYPE_NOT_FOUND)
    })
    @DeleteMapping("/productTypes/{ptid}")
    public BaseResponse deleteProductType(@PathVariable("ptid") Integer productTypeID) {
        return productTypeService.deleteProductType(productTypeID);
    }
}

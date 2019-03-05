package com.spring.baseproject.modules.demo_jpa.controllers;

import com.spring.baseproject.annotations.swagger.Response;
import com.spring.baseproject.annotations.swagger.Responses;
import com.spring.baseproject.base.controllers.BaseRESTController;
import com.spring.baseproject.base.models.BaseResponse;
import com.spring.baseproject.constants.NumberConstants;
import com.spring.baseproject.constants.ResponseValue;
import com.spring.baseproject.constants.StringConstants;
import com.spring.baseproject.swagger.BaseResponseBodySwagger;
import com.spring.baseproject.swagger.demo_jpa.demo_jpa_controller.ProductDtoSwagger;
import com.spring.baseproject.swagger.demo_jpa.demo_jpa_controller.PageProductDtosSwagger;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/demo-jpa")
public class DemoJPAController extends BaseRESTController {

//    @ApiOperation(value = "Get list items", response = Iterable.class)
//    @Responses(value = {
//            @Response(responseValue = ResponseValue.SUCCESS, responseBody = PageProductDtosSwagger.class)
//    })
//    @GetMapping("/items")
//    public BaseResponse getItems(@RequestParam(value = StringConstants.SORT_BY, defaultValue = "") List<String> sortBy,
//                                 @RequestParam(value = StringConstants.SORT_BY, defaultValue = "") List<String> sortType,
//                                 @RequestParam(value = StringConstants.PAGE_INDEX, defaultValue = "1") int pageIndex,
//                                 @RequestParam(value = StringConstants.PAGE_SIZE, defaultValue = NumberConstants.MAX_PAGE_SIZE+"") int pageSize) {
//
//    }

//    @ApiOperation(value = "Get item detail", response = Iterable.class)
//    @Responses(value = {
//            @Response(responseValue = ResponseValue.SUCCESS, responseBody = ProductDtoSwagger.class)
//    })
//    @GetMapping("/item/{iid}")
//    public BaseResponse getItemDetail(@PathVariable("iid") String itemID) {
//
//    }

//    @ApiOperation(value = "Add new item", response = Iterable.class)
//    @Responses(value = {
//            @Response(responseValue = ResponseValue.SUCCESS, responseBody = BaseResponseBodySwagger.class)
//    })
//    @GetMapping("/item")
//    public BaseResponse addItem() {
//
//    }

//    @ApiOperation(value = "Update item", response = Iterable.class)
//    @Responses(value = {
//            @Response(responseValue = ResponseValue.SUCCESS, responseBody = BaseResponseBodySwagger.class),
//            @Response(responseValue = ResponseValue.ITEM_NOT_FOUND)
//    })
//    @GetMapping("/item/{iid}")
//    public BaseResponse updateItem(@RequestParam("iid") String itemID,
//                                   @RequestBody Item item) {
//
//    }

//    public BaseResponse deleteItem() {
//
//    }
}

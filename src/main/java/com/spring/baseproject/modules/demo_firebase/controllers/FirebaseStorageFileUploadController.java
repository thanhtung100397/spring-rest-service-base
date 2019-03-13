package com.spring.baseproject.modules.demo_firebase.controllers;

import com.spring.baseproject.annotations.swagger.Response;
import com.spring.baseproject.annotations.swagger.Responses;
import com.spring.baseproject.base.controllers.BaseRESTController;
import com.spring.baseproject.base.models.BaseResponse;
import com.spring.baseproject.constants.ResponseValue;
import com.spring.baseproject.modules.demo_firebase.services.FirebaseStorageFileUploadService;
import com.spring.baseproject.swagger.demo_firebase.DownloadUrlDtoSwagger;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/firebase-demo/firebase-storage")
@Api(description = "Upload tệp tin lên demo_firebase storage")
public class FirebaseStorageFileUploadController extends BaseRESTController {
    @Autowired
    private FirebaseStorageFileUploadService firebaseStorageFileUploadService;

    @ApiOperation(value = "Upload tệp tin được chọn lên demo_firebase storage", response = Iterable.class)
    @Responses(value = {
            @Response(responseValue = ResponseValue.SUCCESS, responseBody = DownloadUrlDtoSwagger.class),
            @Response(responseValue = ResponseValue.FIREBASE_STORAGE_UPLOAD_ERROR)
    })
    @PostMapping("/files-upload")
    public BaseResponse uploadFileToFirebaseStorage(@RequestParam(value = "file", required = false) MultipartFile file) {
        return firebaseStorageFileUploadService.uploadFileToFirebaseStorage(file);
    }
}

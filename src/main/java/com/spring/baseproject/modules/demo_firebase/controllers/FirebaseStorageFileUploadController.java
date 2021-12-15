package com.spring.baseproject.modules.demo_firebase.controllers;

import com.spring.baseproject.annotations.swagger.Response;
import com.spring.baseproject.annotations.swagger.Responses;
import com.spring.baseproject.constants.ResponseValue;
import com.spring.baseproject.exceptions.ResponseException;
import com.spring.baseproject.modules.demo_firebase.models.dtos.DownloadUrlDto;
import com.spring.baseproject.modules.demo_firebase.services.FirebaseStorageFileUploadService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/firebase-demo/firebase-storage")
@Api(description = "Upload tệp tin lên demo_firebase storage")
public class FirebaseStorageFileUploadController {
    @Autowired
    private FirebaseStorageFileUploadService firebaseStorageFileUploadService;

    @ApiOperation(value = "Upload tệp tin được chọn lên demo_firebase storage", response = Iterable.class)
    @Responses(value = {
            @Response(responseValue = ResponseValue.FIREBASE_STORAGE_UPLOAD_ERROR)
    })
    @PostMapping("/files-upload")
    public DownloadUrlDto uploadFileToFirebaseStorage(
            @RequestParam(value = "file", required = false) MultipartFile file
    ) throws ResponseException {
        return firebaseStorageFileUploadService.uploadFileToFirebaseStorage(file);
    }
}

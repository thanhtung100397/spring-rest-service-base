package com.spring.baseproject.modules.demo_firebase.services;

import com.spring.baseproject.base.models.BaseResponse;
import com.spring.baseproject.constants.ApplicationConstants;
import com.spring.baseproject.constants.ResponseValue;
import com.spring.baseproject.modules.demo_firebase.models.dtos.DownloadUrlDto;
import com.spring.baseproject.modules.firebase.services.FirebaseStorageUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class FirebaseStorageFileUploadService {
    @Autowired
    private FirebaseStorageUploadService firebaseStorageUploadService;

    public BaseResponse uploadFileToFirebaseStorage(MultipartFile multipartFile) {
        try {
            String contentType = multipartFile.getContentType();
            String storageDir;
            if (contentType.startsWith("image")) {
                storageDir = "images";
            } else {
                storageDir = "files";
            }
            String downloadUrl = firebaseStorageUploadService.upload(
                    ApplicationConstants.BASE_PACKAGE_NAME + "/" + storageDir,
                    multipartFile.getOriginalFilename(), multipartFile.getBytes(), contentType);
            return new BaseResponse(ResponseValue.SUCCESS, new DownloadUrlDto(downloadUrl));
        } catch (IOException e) {
            return new BaseResponse(ResponseValue.FIREBASE_STORAGE_UPLOAD_ERROR);
        }
    }
}

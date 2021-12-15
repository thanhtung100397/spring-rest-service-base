package com.spring.baseproject.modules.demo_firebase.services;

import com.spring.baseproject.constants.ApplicationConstants;
import com.spring.baseproject.constants.ResponseValue;
import com.spring.baseproject.exceptions.ResponseException;
import com.spring.baseproject.modules.demo_firebase.models.dtos.DownloadUrlDto;
import com.spring.baseproject.modules.firebase.services.FirebaseStorageUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class FirebaseStorageFileUploadService {
    public static final String IMAGE_CONTENT_TYPE_GROUP = "image";
    public static final String IMAGES_DIR = "images";
    public static final String FILES_DIR = "files";

    @Autowired
    private FirebaseStorageUploadService firebaseStorageUploadService;

    public DownloadUrlDto uploadFileToFirebaseStorage(MultipartFile multipartFile) throws ResponseException {
        try {
            String contentType = multipartFile.getContentType();
            String storageDir = IMAGE_CONTENT_TYPE_GROUP.equals(contentType)? IMAGES_DIR : FILES_DIR;
            String downloadUrl = firebaseStorageUploadService.upload(
                    ApplicationConstants.BASE_PACKAGE_NAME + "/" + storageDir,
                    multipartFile.getOriginalFilename(), multipartFile.getBytes(), contentType);
            return new DownloadUrlDto(downloadUrl);
        } catch (IOException e) {
            throw new ResponseException(ResponseValue.FIREBASE_STORAGE_UPLOAD_ERROR);
        }
    }
}

package com.spring.baseproject.modules.firebase.services;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.StorageException;
import com.google.firebase.cloud.StorageClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class FirebaseStorageUploadService {
    @Value("${application.firebase.storage.api:http://storage.googleapis.com}")
    private String googleStorageApi;

    public String upload(String dir, String fileName,
                         byte[] data, String contentType) throws StorageException {
        Blob file = StorageClient.getInstance().bucket()
                .create(dir + "/" + fileName, data, contentType);
        return googleStorageApi + "/" + file.getBucket() + "/" + fileName;
    }
}

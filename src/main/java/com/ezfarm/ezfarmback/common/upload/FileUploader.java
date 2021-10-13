package com.ezfarm.ezfarmback.common.upload;

import com.amazonaws.services.s3.model.ObjectMetadata;
import java.io.IOException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class FileUploader {

    private final S3Connector s3Connector;

    public String storeFile(MultipartFile multipartFile) {
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(multipartFile.getContentType());

        String storeFileName = createStoreFileName(multipartFile.getOriginalFilename());
        try {
            s3Connector.storeFile(multipartFile.getInputStream(), objectMetadata, storeFileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return s3Connector.getStoreFileUrl(storeFileName);
    }

    public void deleteFile(String storeFileUrl) {
        String storeFileName = extractStoreFileName(storeFileUrl);
        s3Connector.deleteStoreFile(storeFileName);
    }

    public static String extractStoreFileName(String storeFileUrl) {
        int pos = storeFileUrl.lastIndexOf("/");
        return storeFileUrl.substring(pos + 1);
    }

    private static String createStoreFileName(String originalFilename) {
        String uuid = UUID.randomUUID().toString();
        String ext = extractExt(originalFilename);
        return uuid + "." + ext;
    }

    private static String extractExt(String originalFilename) {
        int pos = originalFilename.lastIndexOf(".");
        return originalFilename.substring(pos + 1);
    }
}

package com.ezfarm.ezfarmback.common.utils.upload;

import com.amazonaws.services.s3.model.ObjectMetadata;
import java.io.IOException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class FileStoreService {

    private final S3Service s3Service;

    public String storeFileToS3(MultipartFile multipartFile) {
        if (multipartFile.isEmpty()) {
            return null;
        }

        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(multipartFile.getContentType());

        String storeFileName = createStoreFileName(multipartFile.getOriginalFilename());
        try {
            s3Service.storeFile(multipartFile.getInputStream(), objectMetadata, storeFileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return s3Service.getStoreFileUrl(storeFileName);
    }

    private static String createStoreFileName(String uploadFileName) {
        String uuid = UUID.randomUUID().toString();
        String ext = extractExt(uploadFileName);
        return uuid + "." + ext;
    }

    private static String extractExt(String originalFilename) {
        int pos = originalFilename.lastIndexOf(".");
        return originalFilename.substring(pos + 1);
    }
}

package com.ezfarm.ezfarmback.common.utils.fileupload;

import static java.lang.String.valueOf;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import java.io.InputStream;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class S3Service {

    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public void storeFile(InputStream inputStream, ObjectMetadata objectMetadata,
        String storeFileName) {
        amazonS3.putObject(new PutObjectRequest(bucket, storeFileName,
            inputStream, objectMetadata).withCannedAcl(
            CannedAccessControlList.PublicRead));
    }

    public void deleteStoreFile(String currentImagePath) {
        amazonS3.deleteObject(new DeleteObjectRequest(bucket, currentImagePath));
    }

    public String getStoreFileUrl(String storeFileName) {
        return valueOf(amazonS3.getUrl(bucket, storeFileName));
    }
}

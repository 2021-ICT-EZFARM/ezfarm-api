package com.ezfarm.ezfarmback.common.fileupload;

import static org.assertj.core.api.Assertions.assertThat;

import com.ezfarm.ezfarmback.common.utils.fileupload.FileStoreService;
import java.io.FileInputStream;
import java.io.IOException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

@DisplayName("파일 업로드 테스트")
@SpringBootTest
public class FileStoreServiceTest {

    @Autowired
    FileStoreService fileStoreService;

    @DisplayName("S3에 파일을 저장한다.")
    @Test
    void storeFile() throws IOException {
        MultipartFile multipartFile = new MockMultipartFile(
            "test.png",
            "origin.png",
            MediaType.IMAGE_JPEG_VALUE,
            new FileInputStream("src/test/resources/images/ezfarm-logo.PNG")
        );

        String storeFileS3Url = fileStoreService.storeFile(multipartFile);

        assertThat(storeFileS3Url).isNotNull();
    }

    @DisplayName("S3에서 파일을 삭제한다.")
    @Test
    void deleteFile() throws IOException {
        MultipartFile multipartFile = new MockMultipartFile(
            "test.png",
            "origin.png",
            MediaType.IMAGE_JPEG_VALUE,
            new FileInputStream("src/test/resources/images/ezfarm-logo.PNG")
        );

        String storeFileS3Url = fileStoreService.storeFile(multipartFile);
        fileStoreService.deleteFile(storeFileS3Url);
        //직접 확인
    }
}

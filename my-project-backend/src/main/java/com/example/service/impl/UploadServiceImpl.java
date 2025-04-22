package com.example.service.impl;

import com.example.entity.vo.response.RichEditorUploadResponse;
import com.example.service.UploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.*;

@Service
@RequiredArgsConstructor
public class UploadServiceImpl implements UploadService {

    private final S3Client s3Client;

    @Value("${aws.bucket-name}")
    private String bucketName;

    @Override
    public RichEditorUploadResponse uploadEditorImages(MultipartFile[] files) {
        if (files == null || files.length == 0) {
            return new RichEditorUploadResponse(1, Map.of("url", Collections.emptyList()));
        }

        List<String> fileUrls = new ArrayList<>();

        for (MultipartFile file : files) {
            try {
                String originalFilename = Objects.requireNonNull(file.getOriginalFilename());
                String ext = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
                String fileName = UUID.randomUUID().toString().replace("-", "") + "." + ext;
                String s3Key = "editor/" + fileName; // S3 中的路径

                PutObjectRequest putRequest = PutObjectRequest.builder()
                        .bucket(bucketName)
                        .key(s3Key)
                        .contentType(file.getContentType())
                        .build();

                s3Client.putObject(putRequest, RequestBody.fromInputStream(file.getInputStream(), file.getSize()));

                // S3 上的访问 URL（如果你的桶是公开的，或者配了 CDN）
                String fileUrl = String.format("https://%s.s3.ap-northeast-1.amazonaws.com/%s", bucketName, s3Key);
                fileUrls.add(fileUrl);
            } catch (IOException e) {
                e.printStackTrace();
                return new RichEditorUploadResponse(1, Map.of("url", Collections.emptyList()));
            }
        }

        return new RichEditorUploadResponse(0, Map.of("url", fileUrls));
    }
}

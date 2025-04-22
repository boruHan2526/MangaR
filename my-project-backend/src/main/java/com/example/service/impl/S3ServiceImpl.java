package com.example.service.impl;

import com.example.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.ListBucketsRequest;
import software.amazon.awssdk.services.s3.model.ListBucketsResponse;

@Service
@RequiredArgsConstructor
public class S3ServiceImpl implements S3Service {

    private final S3Client s3Client;

    @Override
    public void listAllBuckets() {
        ListBucketsRequest request = ListBucketsRequest.builder().build();
        ListBucketsResponse response = s3Client.listBuckets(request);
        response.buckets().forEach(bucket -> System.out.println(bucket.name()));
    }
}

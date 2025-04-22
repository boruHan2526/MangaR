package com.example.controller;

import com.example.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/s3")
public class S3Controller {

    private final S3Service s3Service;

    @GetMapping("/buckets")
    public String listBuckets() {
        s3Service.listAllBuckets();
        return "Bucket names printed to console.";
    }
}

package com.example.service;

import com.example.entity.vo.response.RichEditorUploadResponse;
import org.springframework.web.multipart.MultipartFile;

public interface UploadService {
    RichEditorUploadResponse uploadEditorImages(MultipartFile[] files);
}

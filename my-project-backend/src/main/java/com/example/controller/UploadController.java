package com.example.controller;

import com.example.entity.vo.response.RichEditorUploadResponse;
import com.example.service.UploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/upload")
public class UploadController {

    @Autowired
    private UploadService uploadService;

    @PostMapping("/rich_editor_upload")
    public RichEditorUploadResponse uploadEditorImages(@RequestParam("files") MultipartFile[] files) {
        return uploadService.uploadEditorImages(files);
    }
}

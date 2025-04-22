package com.example.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/upload/**")
                .addResourceLocations("file:" + System.getProperty("user.dir") + "/public/upload/");
    }
}

// 这个配置是 必须的，原因是你希望通过 HTTP 请求访问存储在服务器本地的文件。具体来说，/upload/** 这样的 URL 请求应该映射到服务器本地的 /public/upload/ 目录中的实际文件。
// 在 Web 应用中，静态资源（如图片、CSS、JavaScript 文件）通常存储在服务器的本地文件系统中，而不是直接嵌入到应用的代码中。为了让前端能够通过浏览器请求这些静态资源，你需要将这些文件暴露到 HTTP 请求中。
// addResourceHandler("/upload/**")：
//
// 这表示你希望对 /upload/** 路径的所有请求进行处理。
// /** 是一个通配符，表示匹配 /upload/ 后面的所有路径（比如 /upload/filename.jpg）。
//
// addResourceLocations("file:" + System.getProperty("user.dir") + "/public/upload/")：
// 这个部分指定了文件存储的实际路径（file: 表示文件路径），告诉 Spring 从哪里加载静态资源。
// System.getProperty("user.dir") + "/public/upload/" 是你上传文件存储的目录。
// Spring 将从这个本地路径加载文件并将它们暴露给前端请求。

// 让文件可访问：假设你的图片文件保存在 /public/upload/ 目录下，而你希望用户通过 HTTP 请求（比如 http://localhost:8080/upload/filename.jpg）访问这些文件。没有这个配置，Spring Boot 不会知道如何映射这些本地文件路径到 HTTP 请求。
// 静态资源访问：Spring Boot 默认会处理 /static、/public、/resources 和 /META-INF/resources 目录下的静态资源，但是如果你存储在其他目录（比如 /public/upload/），你就需要显式地配置 ResourceHandlerRegistry 来告诉 Spring 这些路径是可访问的。
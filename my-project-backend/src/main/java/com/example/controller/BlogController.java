package com.example.controller;

import com.example.entity.RestBean;
import com.example.entity.dto.Blog;
import com.example.service.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/blogs")
public class BlogController {

    @Autowired
    private BlogService blogService;

    @GetMapping
    public RestBean<List<Blog>> getAllBlogs() {
        List<Blog> blogs = blogService.getAllBlogs();
        return RestBean.success(blogs);
    }

    @GetMapping("/getAllAltBlogs")
    public RestBean<List<Blog>> getAllAltBlogs() {
        List<Blog> blogs = blogService.getAllAltBlogs();
        return RestBean.success(blogs);
    }

    @GetMapping("/{id}")
    public RestBean<Blog> getBlogById(@PathVariable("id") Long id) {
        Blog blog = blogService.getBlogById(id);
        return RestBean.success(blog);
    }

    @PostMapping("/save")
    public RestBean<String> saveBlog(@RequestBody Blog blog) {
        blogService.save(blog);
        return RestBean.success();
    }

    // 这个方法目前不会用到
    // 根据分类筛选，V1.0做的纯前端分类。全部取出后匹配显示。
    // 但之后有升级的可能性，先放在这里
    @GetMapping("/category/{category}")
    public RestBean<List<Blog>> getBlogsByCategory(@PathVariable String category) {
        List<Blog> blogs = blogService.getBlogsByCategory(category);
        return RestBean.success(blogs);
    }

}

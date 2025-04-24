package com.example.service;

import com.example.entity.dto.Blog;

import java.util.List;

public interface BlogService {
    public List<Blog> getAllBlogs();

    public List<Blog> getBlogsByCategory(String category);

    public  Blog getBlogById(Long id);

    void save(Blog blog);

    List<Blog> getAllAltBlogs();
}

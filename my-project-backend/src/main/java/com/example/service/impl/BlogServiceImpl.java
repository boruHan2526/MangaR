package com.example.service.impl;

import com.example.entity.dto.Blog;
import com.example.mapper.BlogMapper;
import com.example.service.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class BlogServiceImpl implements BlogService {

    @Autowired
    private BlogMapper blogMapper;

    public List<Blog> getAllBlogs(){
        return blogMapper.findAll();
    }

    public List<Blog> getBlogsByCategory(String category) {
        return blogMapper.findByCategory(category);
    }

    @Override
    public Blog getBlogById(Long id) {
        return blogMapper.getBlogById(id);
    }

    @Override
    public void save(Blog blog) {
        blogMapper.insert(blog);
    }

    @Override
    public List<Blog> getAllAltBlogs() {
        return blogMapper.findAllAlt();
    }

}

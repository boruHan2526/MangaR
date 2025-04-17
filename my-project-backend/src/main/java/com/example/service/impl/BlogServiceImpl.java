package com.example.service.impl;

import com.example.entity.dto.Blog;
import com.example.mapper.BlogMapper;
import com.example.service.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

}

package com.example.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class Blog {
    private Long id;
    private String title;
    private String description;
    private String category;
    private LocalDateTime date;
    private String content;
}

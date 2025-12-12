package com.example.pesysserver.pojo.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class VideoFile {
    private Integer vidoeId;
    private String name;

    // 扩展字段
    private String fileName;
    private String filePath;
    private String originalName;
    private Long fileSize;
    private Integer classId;
    private String className;
    private Integer studentId;
    private String studentName;
    private String examProject;
    private String analysisStatus;
    private Double aiScore;
    private String analysisResult;
    private LocalDateTime uploadTime;
    private LocalDateTime analysisTime;
    private String uploadedBy;
}
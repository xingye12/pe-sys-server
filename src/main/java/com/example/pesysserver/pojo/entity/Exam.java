package com.example.pesysserver.pojo.entity;

import lombok.Data;

import java.util.Date;

@Data
public class Exam {
    private int examId;
    private String type;
    private String description;
    private int classId;
    private String examName;
    private Date beginTime;
    private Date endTime;
    private String state;
}

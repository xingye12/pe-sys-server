package com.example.pesysserver.pojo.entity;

import lombok.Data;

@Data
public class Teacher {
    private int teacherId;
    private String name;
    private String password;
    private int classId;
}

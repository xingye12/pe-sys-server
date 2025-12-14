package com.example.pesysserver.pojo.entity;

import lombok.Data;

@Data
public class TeacherEntity {
    private int teacherId;
    private String username;
    private String name;
    private String password;
    private int classId;
}

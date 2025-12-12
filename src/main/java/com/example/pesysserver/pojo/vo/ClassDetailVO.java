package com.example.pesysserver.pojo.vo;

import lombok.Data;

import java.util.List;

@Data
public class ClassDetailVO {
    private Long id;
    private String className;
    private String grade;
    private String teacherName;
    private Integer studentCount;
    private List<StudentInfo> students;
    private List<ExamTaskInfo> examTasks;

    @Data
    public static class StudentInfo {
        private Long id;
        private String studentId;
        private String name;
        private String gender;
        private Integer age;
    }

    @Data
    public static class ExamTaskInfo {
        private Long id;
        private String taskName;
        private String examDate;
        private String status;
    }
}
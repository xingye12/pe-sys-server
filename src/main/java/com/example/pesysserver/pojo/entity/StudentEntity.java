package com.example.pesysserver.pojo.entity;

import lombok.Data;

@Data
//适配修改后的数据库,现在用这个吧,username是账号（其实应该叫account,但是习惯了）
public class StudentEntity {
    private int studentId;
    private String username;
    private String name;
    private String password;
    private int classId;
}

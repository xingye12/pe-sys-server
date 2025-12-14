package com.example.pesysserver.controller;

import com.example.pesysserver.pojo.dto.LoginRequestDTO;
import com.example.pesysserver.service.LoginService;
import com.example.pesysserver.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/login")
public class LoginController {

    @Autowired
    private LoginService loginService;

    @PostMapping
    public Result login(@RequestBody LoginRequestDTO loginRequest) {
        return loginService.login(loginRequest);
    }
}
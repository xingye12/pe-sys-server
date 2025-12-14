package com.example.pesysserver.service;

import com.example.pesysserver.pojo.dto.LoginRequestDTO;
import com.example.pesysserver.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

public interface LoginService {
    Result login(LoginRequestDTO loginRequest);
}

package com.example.pesysserver.config;

import com.example.pesysserver.handler.ListStringTypeHandler;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.List;

@Configuration
public class MyBatisConfig {

    @Autowired
    private SqlSessionFactory sqlSessionFactory;

    @PostConstruct
    public void registerTypeHandler() {
        org.apache.ibatis.session.Configuration configuration = sqlSessionFactory.getConfiguration();
        // 注册类型处理器，用于处理 List<String> 类型的字段
        configuration.getTypeHandlerRegistry().register(List.class, new ListStringTypeHandler());
    }
}
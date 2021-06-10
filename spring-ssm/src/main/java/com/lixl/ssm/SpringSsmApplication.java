package com.lixl.ssm;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(basePackages = {"com.lixl.ssm.dao"})
public class SpringSsmApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringSsmApplication.class, args);
    }

}

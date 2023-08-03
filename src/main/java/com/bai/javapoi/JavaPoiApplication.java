package com.bai.javapoi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tk.mybatis.spring.annotation.MapperScan;


@SpringBootApplication
@MapperScan("com.bai.javapoi.mapper")
public class JavaPoiApplication {

    public static void main(String[] args) {
        SpringApplication.run(JavaPoiApplication.class, args);
    }

}

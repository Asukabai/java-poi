package com.bai.javapoi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import tk.mybatis.spring.annotation.MapperScan;


/**
 *  启用定时任务,定时删除服务器上的文件
 *
 *  @author zhangxuejin
 */
@SpringBootApplication
@MapperScan("com.bai.javapoi.mapper")
@EnableScheduling
public class JavaPoiApplication {

    public static void main(String[] args) {
        SpringApplication.run(JavaPoiApplication.class, args);
    }
}

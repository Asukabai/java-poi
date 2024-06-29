package com.bai.javapoi.service;



import com.bai.javapoi.pojo.dto.RespondDto;
import org.springframework.web.multipart.MultipartFile;

public interface FileFixService {

    /**
     * 修改文件上传接口
     */
    RespondDto upLoadFiles(MultipartFile[] files, String[] types);


    /**
     * 一键退回（将第一个路径下的配置文件内容清空）
     */
    RespondDto backFiles();

}

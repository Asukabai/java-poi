package com.bai.javapoi.service;



import com.bai.javapoi.pojo.dto.RespondDto;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URISyntaxException;

public interface FileService {

    /**
     * Excel文件上传接口
     */
    RespondDto upLoadFiles(MultipartFile[] files, String[] types);

    /**
     * Excel文件导出接口
     */
    RespondDto writeExcel(HttpServletResponse response , String[] fileUrls) throws IOException, URISyntaxException;

}

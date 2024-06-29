package com.bai.javapoi.controller;


import com.bai.javapoi.pojo.dto.RespondDto;
import com.bai.javapoi.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URISyntaxException;


/**
 *
 * @author zhangxuejin
 *
 */

@CrossOrigin
@RestController
@RequestMapping("/manage/server")
public class FileController {

    @Resource
    private FileService fileService;

    /**
     * 1、接受前端传入的文件数组和文件类型对应的数组【文件和类型数组要一一对应】，下载到本地，并将下载到本地的文件路径返回给前端
     * 2、url: http://localhost:9001/manage/server/uploadExcels
     * @param multipartFiles
     * @param types
     */

    @PostMapping(value = "/uploadExcels")
    public RespondDto upLoadFiles(@NonNull @RequestParam("multipartFiles") MultipartFile[] multipartFiles,
                                  @NonNull @RequestParam("types") String[] types){
        return fileService.upLoadFiles(multipartFiles,types);
    }

    /**
     * 1、接受前端传入的文件路径参数，对文件进行解析，最终生成/导出一个最终的Excel文件
     * 2、url: http://localhost:9001/manage/server/writeExcel
     * @param fileUrls
     * @throws IOException
     * @throws URISyntaxException
     */

    @GetMapping(value = "/writeExcel")
    public void writeExcel(HttpServletResponse response ,
                           @RequestParam("fileUrls") String[] fileUrls) throws IOException, URISyntaxException {
        fileService.writeExcel(response,fileUrls);
    }
}

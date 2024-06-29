package com.bai.javapoi.controller;

import com.bai.javapoi.pojo.dto.RespondDto;
import com.bai.javapoi.service.FileFixService;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;


/**
 *
 * @author zhangxuejin
 *
 */

@CrossOrigin
@RestController
@RequestMapping("/manage/server")
public class FileFixController {

    @Resource
    private FileFixService fileFixService;


    @PostMapping(value = "/fixExcels")
    public RespondDto upLoadFiles(@NonNull @RequestParam("multipartFiles") MultipartFile[] multipartFiles,
                                  @NonNull @RequestParam("types") String[] types){
        return fileFixService.upLoadFiles(multipartFiles,types);
    }

    @PostMapping(value = "/backFiles")
    public RespondDto backFiles(){
        return fileFixService.backFiles();
    }
}

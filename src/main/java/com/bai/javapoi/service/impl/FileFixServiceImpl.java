package com.bai.javapoi.service.impl;

import com.bai.javapoi.pojo.dto.RespondDto;
import com.bai.javapoi.service.FileFixService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.util.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
public class FileFixServiceImpl implements FileFixService {

    @Value("${absolutePath}")
    private String absolutePath;

    /**
     * 实现配置文件一次性上传功能【上传配置文件.properties 和 模板Excel，和每个上传文件的类型】
     *
     * 1.主要通过对配置文件的覆盖，实现对配置文件的更新功能
     * 2.注意要将文件流关闭
     */

    @Override
    public RespondDto upLoadFiles(MultipartFile[] files, String[] types) {
        List<String> fileUrls = new ArrayList<>();
        // 将每次上传的Excel文件放到指定的文件夹下
        File dir = new File(absolutePath);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        // 存储已上传的type类型
        Set<String> uploadedTypes = new HashSet<>();

        for (int i = 0; i < files.length; i++) {
            MultipartFile file = files[i];
            String type = types[i];
            // 判断文件或类型不能为空，不能上传空文件
            if (file.isEmpty() || StringUtils.isEmpty(type)) {
                return new RespondDto<>("文件或类型不能为空，请重新上传！");
            }
            String originalFileName = file.getOriginalFilename();
            // 检查type是否已存在
            if (uploadedTypes.contains(type)) {
                return new RespondDto<>("上传文件的type类型重复，请重新上传！");
            }
            uploadedTypes.add(type);
            // 获取文件名和扩展名
            String fileExt = originalFileName.substring(originalFileName.lastIndexOf("."));
            File fileToSave = new File(dir.getAbsolutePath() + File.separator + type + fileExt);

            // 检查文件是否存在，存在则删除旧文件
            if (fileToSave.exists()) {
                fileToSave.delete();
            }
            FileOutputStream os=null;
            try {
                os =new FileOutputStream(fileToSave);
                IOUtils.copy(file.getInputStream(),os);
                String fileUrl = fileToSave.getAbsolutePath();
                fileUrls.add(fileUrl);
            } catch (Exception e) {
                e.printStackTrace();
                return new RespondDto<>("文件上传失败！");
            }finally{
                if(os!=null){
                    IOUtils.closeQuietly(os);
                }
            }
            log.info("【源文件】" + originalFileName + "已保存到本地，【文件名】是 " + type + " 【存放路径是】 " + fileToSave.getAbsolutePath());
        }
        return new RespondDto<>(fileUrls);
    }


    /**
     * 实现一键回退功能，退回至上一个版本。
     *
     * 1.通过将原来放置配置文件路径下的配置文件的内容清空，使得在加载配置文件的时候，必须去加载上一版配置文件的内容
     * 2.最终实现版本回退的效果
     * @return
     */

    @Override
    public RespondDto backFiles()  {

        File directory = new File(absolutePath);

        if (directory.exists() && directory.isDirectory()) {

            // 获取满足条件的文件列表
            File[] files = directory.listFiles((dir, name) -> name.endsWith(".properties"));

            if (files != null && files.length > 0) {
                for (File file : files) {
                    try (FileWriter fw = new FileWriter(file)) {
                        // 将配置文件内容清空
                        fw.write("");
                        fw.flush();
                    } catch (IOException e) {
                        // 处理写入文件时的IO异常
                        log.info("清空配置文件内容时发生IO异常：" + e.getMessage());
                        e.printStackTrace();
                    }
                }
            } else {
                // 没有找到符合条件的配置文件
                log.info("指定目录下没有以'.properties'结尾的配置文件。");
            }
        } else {
            // 目录不存在或不是一个目录，进行错误处理
            log.info("指定的目录路径不存在或不是一个目录。");
        }
        return null;
    }
}
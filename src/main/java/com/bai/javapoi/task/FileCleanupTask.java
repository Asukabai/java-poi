package com.bai.javapoi.task;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.chrono.ChronoLocalDate;
import java.time.format.DateTimeFormatter;

@Component // 表示将该类声明为一个组件，以便能够被Spring容器管理。
public class FileCleanupTask {

    @Value("${absolutePath}")
    private String absolutePath;

    // @Scheduled(cron = "0 */1 * * * *") // 每隔1秒触发任务
    @Scheduled(cron = "0 0 0 * * ?") // 每隔1天触发任务
    public void cleanupFiles() {
        // 修改为你的上传文件存放的目录路径
        String directoryPath = absolutePath + File.separator + "uploadFile";
        File directory = new File(directoryPath);
        // 检查目录是否存在
        if (directory.exists() && directory.isDirectory()) {
            LocalDateTime twoMinutesAgo = LocalDateTime.now().minusMinutes(1);
            deleteOlderFiles(directory, twoMinutesAgo);
        }
    }
    private void deleteOlderFiles(File directory, LocalDateTime twoMinutesAgo) {
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    // 获取文件夹的名称，格式为"yyyy-MM-ddXXX"，其中XXX为随机数
                    String folderName = file.getName();
                    String dateString = folderName.substring(0, 10);
                    LocalDate folderDate = LocalDate.parse(dateString, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                    // 如果文件的创建时间早于两分钟前，则删除文件
                    if (folderDate.isBefore(ChronoLocalDate.from(twoMinutesAgo))) {
                        deleteDirectory(file);
                        System.out.println("Deleted file: " + file.getAbsolutePath());
                    }
                }
            }
        }
    }
    private void deleteDirectory(File directory) {
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    deleteDirectory(file);
                } else {
                    file.delete();
                }
            }
        }
        directory.delete();
    }
}
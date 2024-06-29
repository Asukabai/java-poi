package com.bai.javapoi.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * @author 32937
 */
@Component
public class ConfigCheck2 {

    @Value("${absolutePath}")
    private String absolutePath;

    private static final Logger log = LoggerFactory.getLogger(ConfigCheck2.class);
    private  Properties config;
    private long configLastModifiedTime;
    @PostConstruct
    public void init() throws IOException {
        configCheck();
    }
    public void configCheck() throws IOException {
        // 加载配置文件
        loadConfigFile();
        // 记录配置文件的最后修改时间
        configLastModifiedTime = getConfigFileLastModifiedTime();
    }
    private void loadConfigFile() throws IOException {
        String configFilePath= absolutePath + File.separator + "configOfCompliance.properties";
        log.info("当前配置文件文件所处的绝对路径为："+configFilePath);
        File configFile = new File(configFilePath);
        if (configFile.exists() && configFile.isFile() && configFile.length() > 0) {
            try (FileInputStream configFileStream = new FileInputStream(configFile)) {
                config = new Properties();
                config.load(configFileStream);
                log.info("使用原始配置文件：" + configFilePath);
                log.info("config配置文件共有："+config.size()+"行");
            } catch (IOException e) {
                log.error("加载原始配置文件时出错: ", e);
            }
        } else {
            String backupConfigFilePath = absolutePath + File.separator + "back" + File.separator + "configOfCompliance.properties";
            log.info("当前备份配置文件文件所处的绝对路径为："+backupConfigFilePath);
            File backupConfigFile = new File(backupConfigFilePath);
            if (backupConfigFile.exists() && backupConfigFile.isFile() && backupConfigFile.length() > 0) {
                try (FileInputStream configFileStream = new FileInputStream(backupConfigFile)) {
                    config = new Properties();
                    config.load(configFileStream);
                    log.info("使用备份配置文件：" + backupConfigFilePath);
                    log.info("config配置文件共有："+config.size()+"行");
                } catch (IOException e) {
                    log.error("加载备份配置文件时出错: ", e);
                }
            } else {
                log.error("无法找到原始配置文件和备份配置文件，请检查文件路径。");
            }
        }
    }
    private long getConfigFileLastModifiedTime() {

        String configFilePath = absolutePath + File.separator + "configOfCompliance.properties";
        File configFile = new File(configFilePath);

        if (configFile.exists() && configFile.isFile()) {
            return configFile.lastModified();
        } else {
            String backupConfigFilePath = absolutePath + File.separator + "back" + File.separator + "configOfCompliance.properties";
            File backupConfigFile = new File(backupConfigFilePath);

            if (backupConfigFile.exists() && backupConfigFile.isFile()) {
                return backupConfigFile.lastModified();
            } else {
                log.error("无法找到原始配置文件和备份配置文件，请检查文件路径。");
                // 返回一个默认值或抛出异常，根据实际需求进行处理
                return 0;
            }
        }
    }
    @Scheduled(fixedDelay = 1000*60) // 每隔一分钟执行一次定时任务
    private void checkConfigFileUpdates() throws IOException {
        // 获取配置文件的最后修改时间
        long lastModifiedTime = getConfigFileLastModifiedTime();

        // 检查配置文件是否有更新
        if (lastModifiedTime > configLastModifiedTime) {
            log.info("configOfCompliance原始配置文件已更新，重新加载配置文件...");
            // 重新加载配置文件
            loadConfigFile();
            // 更新配置文件的最后修改时间
            configLastModifiedTime = lastModifiedTime;
        }
    }
    public Properties getConfig() {
        return config;
    }
}
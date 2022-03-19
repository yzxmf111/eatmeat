package com.xiaotian.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * @author xiaotian
 * @description
 * @date 2022-03-17 23:23
 */
@Component
@ConfigurationProperties(prefix = "file")
@PropertySource("classpath:files-upload.properties")
public class FileUploadConfig {

    private String fileUploadPath;

    public String getFileUploadPath() {
        return fileUploadPath;
    }

    public void setFileUploadPath(String fileUploadPath) {
        this.fileUploadPath = fileUploadPath;
    }
}

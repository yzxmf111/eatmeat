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
@PropertySource("classpath:files-upload-prod.properties")
public class FileUploadConfig {

    private String fileUploadPath;
    private String serverPath;

    public String getFileUploadPath() {
        return fileUploadPath;
    }

    public void setFileUploadPath(String fileUploadPath) {
        this.fileUploadPath = fileUploadPath;
    }

    public String getServerPath() {
        return serverPath;
    }

    public void setServerPath(String serverPath) {
        this.serverPath = serverPath;
    }
}

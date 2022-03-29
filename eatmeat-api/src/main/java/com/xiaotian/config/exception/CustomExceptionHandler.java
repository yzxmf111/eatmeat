package com.xiaotian.config.exception;

import com.xiaotian.utils.Response;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

/**
 * @description:
 * @author: Tian
 * @time: 2022/3/19 23:40
 */

@RestControllerAdvice
public class CustomExceptionHandler {

    // 上传文件超过100k，捕获异常：MaxUploadSizeExceededException
    //让提示更加友好
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public Response fileMaxExceptionHandler() {
        return Response.errorMsg("文件上传大小不能超过100k，请压缩图片或者降低图片质量再上传！");
    }
}

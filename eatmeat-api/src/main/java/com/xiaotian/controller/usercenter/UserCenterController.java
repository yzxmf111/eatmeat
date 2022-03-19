package com.xiaotian.controller.usercenter;

import com.xiaotian.config.FileUploadConfig;
import com.xiaotian.controller.BaseController;
import com.xiaotian.enums.ErrorMessage;
import com.xiaotian.pojo.User;
import com.xiaotian.service.usercenter.UserInfoService;
import com.xiaotian.utils.Response;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author xiaotian
 * @description 为了方便以后服务的拆分
 * @date 2022-02-28 23:10
 */
@Api(value = "个人用户中心api", tags = {"个人用户中心api"})
@RestController
@RequestMapping("center")
public class UserCenterController extends BaseController {

    @Autowired
    private UserInfoService userInfoService;

    @Autowired
    private FileUploadConfig fileUploadConfig;

    @ApiOperation(value = "查询用户信息", notes = "根据用户id查询用户信息", httpMethod = "GET")
    @GetMapping("userInfo")
    public Response userInfo(
            @ApiParam(name = "用户id", value = "用户id", required = true) String userId) {
        if (StringUtils.isEmpty(userId)) {
            return Response.errorMsg(ErrorMessage.PARAM_ERROR.getMessage());
        }
        User user = userInfoService.queryUserInfo(userId);
        return Response.ok(user);
    }

    @ApiOperation(value = "修改用户信息", notes = "根据用户id修改用户信息", httpMethod = "POST")
    @PostMapping("update")
    public Response userInfo(
            @ApiParam(name = "用户id", value = "用户id", required = true) String userId,
            @RequestBody @Valid User user, BindingResult result) {
        if (StringUtils.isEmpty(userId)) {
            return Response.errorMsg(ErrorMessage.PARAM_ERROR.getMessage());
        }
        if (result.hasErrors()) {
            //如果没有通过,跳转提示
            Map<String, String> map = getErrors(result);
            return Response.errorMap(map);
        }
        /**
         * 正常来说，前端做了user的字段校验，后端也是要做的
         */
        userInfoService.updateUserInfo(user, userId);


        //userResult = setNullProperty(userResult);
        //CookieUtils.setCookie(request, response, "user",
        //        JsonUtils.objectToJson(userResult), true);

        // TODO 后续要改，增加令牌token，会整合进redis，分布式会话
        return Response.ok("");
    }

    private Map<String, String> getErrors(BindingResult result) {
        Map<String, String> map = new HashMap<String, String>();
        List<FieldError> list = result.getFieldErrors();
        for (FieldError error : list) {
            //System.out.println("error.getField():" + error.getField());
            //System.out.println("error.getDefaultMessage():" + error.getDefaultMessage());
            map.put(error.getField(), error.getDefaultMessage());
        }

        //File a = new File("/usr/ddd/aaa");
        //a.getParentFile().mkdirs()
        return map;
    }

    @ApiOperation(value = "用户头像上传", notes = "用户头像上传", httpMethod = "POST")
    @PostMapping("uploadFace")
    public Response uploadUserFace(
            @ApiParam(name = "用户id", value = "用户id", required = true) String userId,
            @ApiParam(name = "用户头像", value = "用户头像", required = true) MultipartFile file) {
        if (StringUtils.isEmpty(userId)) {
            return Response.errorMsg(ErrorMessage.PARAM_ERROR.getMessage());
        }
        if (file == null) {
            return Response.errorMsg(ErrorMessage.PARAM_ERROR.getMessage());
        }
        InputStream inputStream = null;
        FileOutputStream outputStream = null;
        //定义文件名 在固定路径下写face-{userId}-当前时间.png/.jpg
        try {
            String originalFilename = file.getOriginalFilename();
            if (StringUtils.isNotBlank(originalFilename)) {
                String[] split = originalFilename.split("\\.");
                //文件名
                String faceName = "face" + "_" + userId + "_" + System.currentTimeMillis() + "." + split[split.length - 1];
                //抽象路径名
                String filePath = fileUploadConfig.getFileUploadPath() + File.separator + userId + File.separator + faceName;
                File outputFile = new File(filePath);
                if (!outputFile.getParentFile().exists()) {
                    outputFile.getParentFile().mkdirs();
                }
                inputStream = file.getInputStream();
                // 文件输出保存到目录
                outputStream = new FileOutputStream(outputFile);
                IOUtils.copy(inputStream, outputStream);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (outputStream != null) {
                    outputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return Response.ok("");
    }
}

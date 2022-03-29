package com.xiaotian.controller.usercenter;

import com.xiaotian.config.FileUploadConfig;
import com.xiaotian.controller.BaseController;
import com.xiaotian.enums.ErrorMessage;
import com.xiaotian.pojo.User;
import com.xiaotian.service.usercenter.UserInfoService;
import com.xiaotian.utils.CookieUtils;
import com.xiaotian.utils.DateUtil;
import com.xiaotian.utils.JsonUtils;
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
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
            @RequestBody @Valid User user, BindingResult result,
            HttpServletRequest request, HttpServletResponse response) {
        if (StringUtils.isEmpty(userId)) {
            return Response.errorMsg(ErrorMessage.PARAM_ERROR.getMessage());
        }
        if (result.hasErrors()) {
            //如果没有通过,跳转提示
            Map<String, String> map = getErrors(result);
            return Response.errorMap(map);
        }

        User userInfo = userInfoService.updateUserInfo(user, userId);
        /**
         * 正常来说，前端做了user的字段校验，后端也是要做的
         */
        userInfo = setNullProperty(userInfo);
        CookieUtils.setCookie(request, response, "user",
                JsonUtils.objectToJson(userInfo), true);

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
            @ApiParam(name = "用户头像", value = "用户头像", required = true) MultipartFile file,
            HttpServletRequest request, HttpServletResponse response) {
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
                String suffix = split[split.length - 1];
                if (!StringUtils.equalsIgnoreCase("jpg", suffix) && !StringUtils.equalsIgnoreCase("png", suffix) &&
                        !StringUtils.equalsIgnoreCase("jpeg", suffix)) {
                    return Response.errorException("请选择正确的图片格式，以jpg, png, jpeg为后缀名");
                }
                String faceName = "face" + "_" + userId + "_" + System.currentTimeMillis() + "." + suffix;
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
                //更新用户头像信息
                String fileUrl = fileUploadConfig.getServerPath() + "/" + userId + "/" + faceName;
                //由于浏览器存在缓存,加上时间戳保证前端页面的即时刷新
                User userInfo = userInfoService.updateUserFace(userId, fileUrl + "?t=" + DateUtil.getCurrentDateString(DateUtil.DATETIME_PATTERN));
                userInfo = setNullProperty(userInfo);
                CookieUtils.setCookie(request, response, "user",
                        JsonUtils.objectToJson(userInfo), true);
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


    private User setNullProperty(User userInfo) {
        if (userInfo == null) {
            return new User();
        }
        //userInfo.setId(null);
        userInfo.setRealname(null);
        userInfo.setPassword(null);
        userInfo.setMobile(null);
        return userInfo;
    }

    public static void main(String[] args) {
        String currentDateTime = DateUtil.getCurrentDateString(DateUtil.DATETIME_PATTERN);
        System.out.println(currentDateTime);
    }
}

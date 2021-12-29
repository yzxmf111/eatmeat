package com.xiaotian.controller;

import com.xiaotian.enums.ErrorEnum;
import com.xiaotian.pojo.UserAddress;
import com.xiaotian.pojo.bo.AddressBO;
import com.xiaotian.service.AddressService;
import com.xiaotian.utils.MobileEmailUtils;
import com.xiaotian.utils.Response;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(value = "地址信息接口", tags = {"地址信息相关接口"})
@RestController
@RequestMapping("address")
public class AddressController {

    @Autowired
    private AddressService addressService;

    @ApiOperation(value = "用户的所有地址信息", notes = "用户的所有地址信息", httpMethod = "POST")
    @PostMapping("add")
    public Response addUserAddress(@RequestBody AddressBO addressBO) {
        if (StringUtils.isEmpty(addressBO)) {
            return Response.errorMsg(ErrorEnum.PARAM_ERROR.desc);
        }
        Response checkResponse = checkParams(addressBO);
        if (checkResponse.getStatus() != 200) {
            return checkResponse;
        }
        addressService.addAddress(addressBO);
        return Response.ok();
    }

    @ApiOperation(value = "用户的所有地址信息", notes = "用户的所有地址信息", httpMethod = "POST")
    @PostMapping("delete")
    public Response deleteUserAddress(@RequestParam String userId,
                                      @RequestParam String addressId) {
        if (StringUtils.isEmpty(userId) || StringUtils.isEmpty(addressId)) {
            return Response.errorMsg(ErrorEnum.PARAM_ERROR.desc);
        }
        addressService.deleteAddress(userId, addressId);
        return Response.ok();
    }


    @ApiOperation(value = "用户的所有地址信息", notes = "用户的所有地址信息", httpMethod = "POST")
    @PostMapping("update")
    public Response updateUserAddress(@RequestBody AddressBO addressBO) {
        if (addressBO == null) {
            return Response.errorMsg(ErrorEnum.PARAM_ERROR.desc);
        }
        if (StringUtils.isEmpty(addressBO.getAddressId())) {
            return Response.errorMsg(ErrorEnum.ADDRESS_ID_PARAM_ERROR.desc);
        }
        Response checkResponse = checkParams(addressBO);
        if (checkResponse.getStatus() != 200) {
            return checkResponse;
        }
        addressService.updateAddress(addressBO);
        return Response.ok();
    }

    private Response checkParams(AddressBO addressBO) {
        String receiver = addressBO.getReceiver();
        if (org.apache.commons.lang3.StringUtils.isBlank(receiver)) {
            return Response.errorMsg("收货人不能为空");
        }
        if (receiver.length() > 12) {
            return Response.errorMsg("收货人姓名不能太长");
        }

        String mobile = addressBO.getMobile();
        if (org.apache.commons.lang3.StringUtils.isBlank(mobile)) {
            return Response.errorMsg("收货人手机号不能为空");
        }
        if (mobile.length() != 11) {
            return Response.errorMsg("收货人手机号长度不正确");
        }
        boolean isMobileOk = MobileEmailUtils.checkMobileIsOk(mobile);
        if (!isMobileOk) {
            return Response.errorMsg("收货人手机号格式不正确");
        }

        String province = addressBO.getProvince();
        String city = addressBO.getCity();
        String district = addressBO.getDistrict();
        String detail = addressBO.getDetail();
        if (org.apache.commons.lang3.StringUtils.isBlank(province) ||
                org.apache.commons.lang3.StringUtils.isBlank(city) ||
                org.apache.commons.lang3.StringUtils.isBlank(district) ||
                org.apache.commons.lang3.StringUtils.isBlank(detail)) {
            return Response.errorMsg("收货地址信息不能为空");
        }

        return Response.ok();
    }

    @ApiOperation(value = "用户的所有地址信息", notes = "用户的所有地址信息", httpMethod = "POST")
    @PostMapping("setDefalut")
    public Response setDefalut(@RequestParam String userId,
                               @RequestParam String addressId) {
        if (StringUtils.isEmpty(userId) || StringUtils.isEmpty(addressId)) {
            return Response.errorMsg(ErrorEnum.PARAM_ERROR.desc);
        }
        addressService.setDefault(userId, addressId);
        return Response.ok();
    }

    @ApiOperation(value = "用户的所有地址信息", notes = "用户的所有地址信息", httpMethod = "POST")
    @PostMapping("list")
    public Response list(@RequestParam String userId) {
        if (StringUtils.isEmpty(userId)) {
            return Response.errorMsg(ErrorEnum.PARAM_ERROR.desc);
        }
        List<UserAddress> list = addressService.list(userId);
        return Response.ok(list);
    }


}




package com.xiaotian.controller;

import com.alibaba.fastjson.JSONObject;
import com.xiaotian.enums.OrderStatusEnum;
import com.xiaotian.enums.PayMethod;
import com.xiaotian.pojo.OrderStatus;
import com.xiaotian.pojo.bo.ShopCatBO;
import com.xiaotian.pojo.bo.SubmitOrderBO;
import com.xiaotian.pojo.vo.MerchantOrdersVO;
import com.xiaotian.pojo.vo.OrderVO;
import com.xiaotian.pojo.vo.ShopCatVO;
import com.xiaotian.service.OrderService;
import com.xiaotian.utils.CookieUtils;
import com.xiaotian.utils.RedisOperator;
import com.xiaotian.utils.Response;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author xiaotian
 * @description
 * @date 2022-01-23 11:45
 */
@Api(value = "订单相关", tags = {"订单相关的api接口"})
@RequestMapping("orders")
@RestController
public class OrdersController extends BaseController {

    final static Logger logger = LoggerFactory.getLogger(OrdersController.class);

    @Autowired
    private OrderService orderService;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private RedisOperator redisOperator;

    @ApiOperation(value = "用户下单", notes = "用户下单", httpMethod = "POST")
    @PostMapping("/create")
    public Response create(
            @RequestBody SubmitOrderBO submitOrderBO,
            HttpServletRequest request,
            HttpServletResponse response) {

        if (submitOrderBO.getPayMethod() != PayMethod.WEIXIN.type
                && submitOrderBO.getPayMethod() != PayMethod.ALIPAY.type) {
            return Response.errorMsg("支付方式不支持！");
        }

//        System.out.println(submitOrderBO.toString());

        // 1. 创建订单
        String submitOrder = redisOperator.get(FOODIE_SHOPCART + ":" + submitOrderBO.getUserId());
        if (StringUtils.isEmpty(submitOrder)) {
            return Response.errorMsg("购物车数据不正确！");
        }
        List<ShopCatBO> shopCatBOList = JSONObject.parseArray(submitOrder, ShopCatBO.class);
        OrderVO orderVO = orderService.createOrder(shopCatBOList, submitOrderBO);
        String orderId = orderVO.getOrderId();

        // 2. 创建订单以后，移除购物车中已结算（已提交）的商品
        List<ShopCatBO> toBeRemoveShopCat = orderVO.getToBeRemoveShopCat();
        if (!CollectionUtils.isEmpty(toBeRemoveShopCat)) {
            shopCatBOList.removeAll(toBeRemoveShopCat);
        }
        redisOperator.set(FOODIE_SHOPCART + ":" + submitOrderBO.getUserId(), JSONObject.toJSONString(shopCatBOList));
        /**
         * 1001
         * 2002 -> 用户购买
         * 3003 -> 用户购买
         * 4004
         */
        // 整合redis之后，完善购物车中的已结算商品清除，并且同步到前端的cookie
        CookieUtils.setCookie(request, response, FOODIE_SHOPCART, JSONObject.toJSONString(shopCatBOList), true);

        // 3. 向支付中心发送当前订单，用于保存支付中心的订单数据
        MerchantOrdersVO merchantOrdersVO = orderVO.getMerchantOrdersVO();
        merchantOrdersVO.setReturnUrl(payReturnUrl);

        // 为了方便测试购买，所以所有的支付金额都统一改为1分钱
        merchantOrdersVO.setAmount(1);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("imoocUserId", "1441139874");
        headers.add("password", "fw4t-r4fg");

        HttpEntity<MerchantOrdersVO> entity =
                new HttpEntity<>(merchantOrdersVO, headers);

        ResponseEntity<Response> responseEntity =
                restTemplate.postForEntity(paymentUrl,
                        entity,
                        Response.class);
        Response paymentResult = responseEntity.getBody();
        if (paymentResult.getStatus() != 200) {
            logger.error("发送错误：{}", paymentResult.getMsg());
            return Response.errorMsg("支付中心订单创建失败，请联系管理员！");
        }

        return Response.ok(orderId);
    }

    @PostMapping("notifyMerchantOrderPaid")
    public Integer notifyMerchantOrderPaid(String merchantOrderId) {
        orderService.updateOrderStatus(merchantOrderId, OrderStatusEnum.WAIT_DELIVER.type);
        return HttpStatus.OK.value();
    }

    @PostMapping("getPaidOrderInfo")
    public Response getPaidOrderInfo(String orderId) {

        OrderStatus orderStatus = orderService.queryOrderStatusInfo(orderId);
        return Response.ok(orderStatus);
    }
}
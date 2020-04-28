package com.online.edu.order.controller;


import com.online.edu.common.vo.R;
import com.online.edu.order.service.PayLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@RestController
@RequestMapping("/orderservice/paylog")
@CrossOrigin
public class PayLogController {
    @Autowired
    PayLogService payLogService;

    // 生成微信支付二维码
    @GetMapping("createNative/{orderNo}")
    public R createNative(@PathVariable String orderNo) {
        Map map = payLogService.createNative(orderNo);
        return R.ok().data(map);
    }

    // 查看支付状态
    @GetMapping("queryPayStataus/{orderNo}")
    public R queryPayStatus(@PathVariable("orderNo") String orderNO) {
        // 调用查询接口

        Map<String, String> map = payLogService.queryPayStatus(orderNO);
        if (map == null) {
            return R.error().message("支付出错");
        }
        if (map.get("trade_state").equals("SUCCESS")) {
            // 如果成功更新订单状态
            payLogService.updateOrderStatus(map);
            return R.ok().message("支付成功");
        }
        return R.ok().code(25000).message("支付中");
    }
}


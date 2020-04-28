package com.online.edu.order.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.online.edu.common.utils.JwtUtils;
import com.online.edu.common.vo.R;
import com.online.edu.order.entity.Order;
import com.online.edu.order.service.OrderService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/orderservice/order")
@CrossOrigin
public class OrderController {
    @Autowired
    private OrderService orderService;

    // 根据课程id和用户id创建订单，返回订单id
    @PostMapping("createOrder/{courseId}")
    public R saveOrder(@PathVariable String courseId, HttpServletRequest request) {
        String memberId = JwtUtils.getMemberIdByJwtToken(request);
        if(org.springframework.util.StringUtils.isEmpty(memberId)) {
            return R.error().code(28004).message("请登录");
        }
        String orderId = orderService.saveOrder(courseId, memberId);
        if (StringUtils.isNotEmpty(orderId)) {
            return R.ok().data("orderId", orderId);
        }
        return R.error().message("订单生成失败");
    }

    @GetMapping("getOrder/{orderId}")
    public R getOrder(@PathVariable String orderId) {
        QueryWrapper<Order> wrapper = new QueryWrapper<>();
        wrapper.eq("order_no", orderId);
        Order order = orderService.getOne(wrapper);
        return R.ok().data("item", order);
    }

    @GetMapping("isBuyCourse/{memberid}/{id}")
    public boolean isBuyCourse(@PathVariable String memberid,
                               @PathVariable String id) {
        //订单状态是1表示支付成功
        int count = orderService.count(new QueryWrapper<Order>().eq("member_id", memberid).eq("course_id", id).eq("status", "1"));
        if (count > 0) {
            return true;
        } else {
            return false;
        }
    }

}

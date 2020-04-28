package com.online.edu.order.service.impl;

import com.online.edu.common.vo.CourseWebVo;
import com.online.edu.common.vo.UserInfoVo;
import com.online.edu.order.client.EduClient;
import com.online.edu.order.client.UcenterClient;
import com.online.edu.order.entity.Order;
import com.online.edu.order.mapper.OrderMapper;
import com.online.edu.order.service.OrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;


@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {
    @Autowired
    EduClient eduClient;

    @Autowired
    UcenterClient ucenterClient;

    @Override
    public String saveOrder(String courseId, String memberId) {
        Order order = new Order();
        CourseWebVo courseDto = eduClient.getCourseInfoDto(courseId);

        UserInfoVo ucenterMember = ucenterClient.getUcenterPay(memberId);

        String outTradeNo =  System.currentTimeMillis() + "";// 将毫秒时间戳拼接到外部订单号
        SimpleDateFormat sdf = new SimpleDateFormat("YYYYMMDDHHmmss");
        outTradeNo = outTradeNo + sdf.format(new Date());// 将时间字符串拼接到外部订单号
        order.setOrderNo(outTradeNo);
        order.setCourseId(courseId);
        order.setCourseTitle(courseDto.getTitle());
        order.setCourseCover(courseDto.getCover());
        order.setTeacherName(courseDto.getTeacherName());
        order.setTotalFee(courseDto.getPrice());
        order.setMemberId(memberId);
        order.setMobile(ucenterMember.getMobile());
        order.setNickname(ucenterMember.getNickname());
        order.setStatus(0);
        order.setPayType(1);
        int insert = baseMapper.insert(order);
        if (insert != 0){
            return order.getOrderNo();
        }
        return null;
    }
}

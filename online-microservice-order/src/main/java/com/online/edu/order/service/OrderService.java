package com.online.edu.order.service;

import com.online.edu.order.entity.Order;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 订单 服务类
 * </p>
 *
 * @author harry
 * @since 2020-04-01
 */
public interface OrderService extends IService<Order> {

    String saveOrder(String courseId, String memberId);
}

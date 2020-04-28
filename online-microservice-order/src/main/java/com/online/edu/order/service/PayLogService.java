package com.online.edu.order.service;

import com.online.edu.order.entity.PayLog;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 * 支付日志表 服务类
 * </p>
 *
 * @author harry
 * @since 2020-04-01
 */
public interface PayLogService extends IService<PayLog> {

    Map createNative(String orderNo);

    Map<String, String> queryPayStatus(String orderNO);

    void updateOrderStatus(Map<String, String> map);
}

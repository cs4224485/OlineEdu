package com.online.edu.order.client;

import com.online.edu.common.vo.UserInfoVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("online-ucenter")
@Component
public interface UcenterClient {
    //根据用户id获取用户信息
    @GetMapping("/ucenter/member/getInfoUc/{memberId}")
    public UserInfoVo getUcenterPay(@PathVariable("memberId") String memberId);
}

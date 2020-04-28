package com.online.edu.ucenter.service;

import com.online.edu.common.vo.LoginVo;
import com.online.edu.common.vo.RegisterVo;
import com.online.edu.ucenter.entity.UcenterMember;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 会员表 服务类
 * </p>
 *
 * @author harry
 * @since 2020-03-17
 */
public interface UcenterMemberService extends IService<UcenterMember> {
    Integer countRegisterByDay(String day);

    UcenterMember getByOpenid(String openid);

    String login(LoginVo loginVo);

    void register(RegisterVo registerVo);
}

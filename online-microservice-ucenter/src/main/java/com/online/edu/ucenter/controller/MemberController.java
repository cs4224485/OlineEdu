package com.online.edu.ucenter.controller;

import com.online.edu.common.vo.UserInfoVo;
import com.online.edu.common.vo.LoginVo;
import com.online.edu.common.vo.R;
import com.online.edu.common.utils.JwtUtils;
import com.online.edu.common.vo.RegisterVo;
import com.online.edu.ucenter.entity.UcenterMember;
import com.online.edu.ucenter.service.UcenterMemberService;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;


@CrossOrigin
@RestController
@RequestMapping("/ucenter/member")
public class MemberController {
    @Autowired
    UcenterMemberService memberService;

    @PostMapping("/login")
    public R Login(@RequestBody LoginVo loginVo){
       String token = memberService.login(loginVo);
       return R.ok().data("token", token);
    }

    @ApiOperation(value = "会员注册")
    @PostMapping("register")
    public R register(@RequestBody RegisterVo registerVo){
        memberService.register(registerVo);
        return R.ok();

    }
    @ApiOperation(value = "获取登录信息")
    @GetMapping("auth/getLoginInfo")
    public R getInfoToken(HttpServletRequest request){
        String memberId  = JwtUtils.getMemberIdByJwtToken(request);
        if(StringUtils.isEmpty(memberId )){
            return R.error().message("认证失效");
        }
        UcenterMember ucenterMember = memberService.getById(memberId);
        String nickname = ucenterMember.getNickname();
        String avatar = ucenterMember.getAvatar();
        String id = memberId;
        UserInfoVo loginInfoVo = new UserInfoVo();
        loginInfoVo.setId(id);
        loginInfoVo.setAvatar(avatar);
        loginInfoVo.setNickname(nickname);
        System.out.println(loginInfoVo);
        return R.ok().data("item", loginInfoVo);
    }

    @GetMapping("getInfoUc/{id}")
    public UserInfoVo getInfo(@PathVariable String id) {
        UcenterMember memberInfo = memberService.getById(id);
        UserInfoVo userInfoVo = new UserInfoVo();
        BeanUtils.copyProperties(memberInfo, userInfoVo);
        return userInfoVo;
    }
}

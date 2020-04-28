package com.online.edu.ucenter.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.online.edu.common.exception.MyException;
import com.online.edu.common.utils.JwtUtils;
import com.online.edu.common.utils.MD5;
import com.online.edu.common.vo.LoginVo;
import com.online.edu.common.vo.RegisterVo;
import com.online.edu.ucenter.entity.UcenterMember;
import com.online.edu.ucenter.mapper.UcenterMemberMapper;
import com.online.edu.ucenter.service.UcenterMemberService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class UcenterMemberServiceImpl extends ServiceImpl<UcenterMemberMapper, UcenterMember> implements UcenterMemberService {

    @Autowired
    RedisTemplate redisTemplate;

    @Override
    public Integer countRegisterByDay(String day) {
        return baseMapper.selectRegisterCount(day);
    }

    @Override
    public UcenterMember getByOpenid(String openid) {
        QueryWrapper<UcenterMember> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("openid", openid);
        UcenterMember member = baseMapper.selectOne(queryWrapper);
        return member;
    }

    @Override
    public String login(LoginVo loginVo) {
        String mobile = loginVo.getMobile();
        String password = loginVo.getPassword();

        if(StringUtils.isEmpty(mobile) || StringUtils.isEmpty(password)){
            throw new MyException(20001, "请输入用户名或密码");
        }
        QueryWrapper<UcenterMember> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("mobile", mobile);
        UcenterMember ucenterMember = baseMapper.selectOne(queryWrapper);
        if (ucenterMember == null){
            throw new MyException(20001, "登录失败，用户名或密码错误");
        }
        if (!MD5.encrypt(password).equals(ucenterMember.getPassword())){
            throw new MyException(20001, "登录失败，用户名或密码错误");
        }
        String token = JwtUtils.getJwtToken(ucenterMember.getId(), ucenterMember.getNickname());
        return token;
    }

    @Override
    public void register(RegisterVo registerVo) {
        //获取注册信息，进行校验
        String nickname = registerVo.getNickname();
        String mobile = registerVo.getMobile();
        String password = registerVo.getPassword();
        String code = registerVo.getCode();
        //校验参数
        if(StringUtils.isEmpty(mobile) || StringUtils.isEmpty(mobile)
                || StringUtils.isEmpty(password) ||StringUtils.isEmpty(code)) {
            throw new MyException(20001,"注册失败");
        }

        // 校验验证码
        Integer redisCode = (Integer) redisTemplate.opsForValue().get(mobile);
        if(!code.equals(redisCode.toString())){
            throw new MyException(20001,"注册失败");
        }
        // 查询数据库中是否存在相同的手机号码
        Integer count = baseMapper.selectCount(new QueryWrapper<UcenterMember>().eq("mobile", mobile));
        if(count.intValue() > 0){
            throw new MyException(20001, "该用户已存在");
        }
        //添加注册信息到数据库
        UcenterMember member = new UcenterMember();
        member.setNickname(nickname);
        member.setMobile(registerVo.getMobile());
        member.setPassword(MD5.encrypt(password));
        member.setDisabled(false);
        member.setAvatar("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1585550343936&di=f7c1235267ff5232f122b0eba96957f8&imgtype=0&src=http%3A%2F%2Fimg3.imgtn.bdimg.com%2Fit%2Fu%3D3999684833%2C1365604362%26fm%3D214%26gp%3D0.jpg");
        this.save(member);
    }
}

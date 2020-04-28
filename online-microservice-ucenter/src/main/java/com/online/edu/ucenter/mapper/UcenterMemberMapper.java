package com.online.edu.ucenter.mapper;

import com.online.edu.ucenter.entity.UcenterMember;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;


public interface UcenterMemberMapper extends BaseMapper<UcenterMember> {
    Integer selectRegisterCount(String day);
}

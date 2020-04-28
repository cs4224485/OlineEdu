package com.online.edu.education.controller;

import com.online.edu.common.vo.R;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/admin")
@CrossOrigin
public class UserController {
    @PostMapping("/user/login")
    public R userLogin() {
        return R.ok().data("token", "admin");
    }
    @GetMapping("/user/info")
    public R userInfo(){
        List<String> roles = new ArrayList<>();
        roles.add("admin");
        return R.ok().data("roles", roles).data("avatar", "https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif").data("name","admin");
    }
}

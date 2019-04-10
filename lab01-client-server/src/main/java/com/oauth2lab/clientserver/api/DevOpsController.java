package com.oauth2lab.clientserver.api;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/api")
public class DevOpsController {
    //模拟 资源服务器中的资源API
    @RequestMapping("/userlist")
    public ResponseEntity<List<UserInfo>> getAllUsers(){
        return ResponseEntity.ok(getUsers());
    }

    private List<UserInfo> getUsers(){
        List<UserInfo> users = new ArrayList<>();
        users.add(new UserInfo("xxx","xxx@qq.com"));
        users.add(new UserInfo("Tom","Tom@qq.com"));
        users.add(new UserInfo("Jerry","jerry@qq.com"));

        return users;
    }
}

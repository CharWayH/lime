package com.charwayh.lime.web.controller;

import com.charwayh.lime.support.domain.User;
import com.charwayh.lime.web.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author charwayH
 */
@RestController
public class LoginController {

    @Autowired
    private UserService userService;

    @PostMapping("/save1")
    public boolean save(@RequestBody User user){
        boolean save = userService.save(user);
        return save;
    }

    @GetMapping("/get1")

    public String get(){
        return "成功";
    }


    @PostMapping("/login1")
    public boolean get(@RequestBody User user){
        return userService.login(user);
    }
}

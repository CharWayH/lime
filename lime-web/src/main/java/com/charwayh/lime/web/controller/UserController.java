package com.charwayh.lime.web.controller;

import com.charwayh.lime.common.vo.BasicResultVO;
import com.charwayh.lime.support.domain.User;
import com.charwayh.lime.web.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author charwayH
 */
@Api(tags = "用户相关接口")
@RequestMapping("/user")
@RestController()
@CrossOrigin(origins = "http://localhost:6565", allowCredentials = "true", allowedHeaders = "*")
public class UserController {

    @Autowired
    private UserService userService;



    @GetMapping("/get")
    public String get(){
        return "成功";
    }

    @PostMapping("/register")
    @ApiOperation("注册用户")
    public BasicResultVO save(@RequestBody User user){
        if(userService.save(user)){
            return BasicResultVO.success("注册成功");
        }
        return BasicResultVO.fail("注册失败");
    }

    @PostMapping("/login")
    @ApiOperation(value = "用户登录")
    public BasicResultVO get(@RequestBody User user){
        if(userService.login(user)){
            return BasicResultVO.success("登录成功！");
        }
        return BasicResultVO.fail("登录失败");
    }
}

package com.charwayh.lime.web.service.impl;

import com.charwayh.lime.support.domain.User;
import com.charwayh.lime.support.mapper.UserMapper;
import com.charwayh.lime.web.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;

/**
 * @author charwayH
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public boolean login(User user) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("username", user.getUsername());
        map.put("password", user.getPassword());
        return !(userMapper.selectByMap(map).isEmpty()) ? true:false;
    }

    @Override
    public boolean save(User user) {
        if(!(existUserByUsername(user)) && (userMapper.insert(user)>=0)){
            return true;
        }
        return false;
    }

    @Override
    public boolean existUserByUsername(User user){
        HashMap<String, Object> map = new HashMap<>();
        map.put("username", user.getUsername());
        return !(userMapper.selectByMap(map).isEmpty());
    }

//    @Override
//    public boolean save(User user) {
//        if (!userDao.findUserByUsername(user.getUsername())){
//            userDao.save(user);
//            return true;
//        }
//        return false;
//    }

}

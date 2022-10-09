package com.charwayh.lime.web.service;


import com.charwayh.lime.support.domain.User;

/**
 * @author charwayH
 */
public interface UserService {

    /**
     * 用户登录
     * @param user
     * @return 判断是否成功
     */
    boolean login(User user);

    /**
     * 注册用户
     * @param user
     * @return
     */
    boolean save(User user);


    /**
     * 根据用户名查看用户是否存在
     * @param user
     * @return
     */
    boolean existUserByUsername(User user);

}

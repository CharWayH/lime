package com.charwayh.lime.cron.xxl.service.impl;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.alibaba.fastjson.JSON;
import com.charwayh.lime.common.enums.RespStatusEnum;
import com.charwayh.lime.common.vo.BasicResultVO;
import com.charwayh.lime.cron.xxl.constants.XxlJobConstant;
import com.charwayh.lime.cron.xxl.entity.XxlJobGroup;
import com.charwayh.lime.cron.xxl.entity.XxlJobInfo;
import com.charwayh.lime.cron.xxl.service.CronTaskService;
import com.google.common.base.Throwables;
import com.xxl.job.core.biz.model.ReturnT;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.HttpCookie;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author charwayH
 */
@Slf4j
@Service
public class CronTaskServiceImpl implements CronTaskService {


    @Value("${xxl.job.admin.username}")
    private String xxlUserName;

    @Value("${xxl.job.admin.password}")
    private String xxlPassword;

    @Value("${xxl.job.admin.addresses}")
    private String xxlAddresses;


    @Override
    public BasicResultVO saveCronTask(XxlJobInfo xxlJobInfo) {
        Map<String,Object> params = JSON.parseObject(JSON.toJSONString(xxlJobInfo),Map.class);
        String path = xxlJobInfo.getId() == null ? xxlAddresses + XxlJobConstant.INSERT_URL
                : xxlAddresses + XxlJobConstant.UPDATE_URL;

        HttpResponse response;
        ReturnT returnT = null;

        try {
            response = HttpRequest.post(path).form(params).cookie(getCookie()).execute();
            returnT = JSON.parseObject(response.body(), ReturnT.class);

            // 插入时需要返回Id，而更新时不需要
            if (response.isOk() && ReturnT.SUCCESS_CODE == returnT.getCode()) {
                if (path.contains(XxlJobConstant.INSERT_URL)) {
                    Integer taskId = Integer.parseInt(String.valueOf(returnT.getContent()));
                    return BasicResultVO.success(taskId);
                } else if (path.contains(XxlJobConstant.UPDATE_URL)) {
                    return BasicResultVO.success();
                }
            }
        } catch (Exception e) {
            log.error("CronTaskService#saveTask fail,e:{},param:{},response:{}", Throwables.getStackTraceAsString(e)
                    , JSON.toJSONString(xxlJobInfo), JSON.toJSONString(returnT));
        }
        return BasicResultVO.fail(RespStatusEnum.SERVICE_ERROR, JSON.toJSONString(returnT));
    }


    @Override
    public BasicResultVO deleteCronTask(Integer taskId) {
        String path = xxlAddresses + XxlJobConstant.DELETE_URL;

        HashMap<String, Object> params = MapUtil.newHashMap();
        params.put("id", taskId);

        HttpResponse response;
        ReturnT returnT = null;
        try {
            response = HttpRequest.post(path).form(params).cookie(getCookie()).execute();
            returnT = JSON.parseObject(response.body(), ReturnT.class);
            if (response.isOk() && ReturnT.SUCCESS_CODE == returnT.getCode()) {
                return BasicResultVO.success();
            }
        } catch (Exception e) {
            log.error("CronTaskService#deleteCronTask fail,e:{},param:{},response:{}", Throwables.getStackTraceAsString(e)
                    , JSON.toJSONString(params), JSON.toJSONString(returnT));
        }
        return BasicResultVO.fail(RespStatusEnum.SERVICE_ERROR, JSON.toJSONString(returnT));
    }

    @Override
    public BasicResultVO startCronTask(Integer taskId) {
        return null;
    }

    @Override
    public BasicResultVO stopCronTask(Integer taskId) {
        return null;
    }

    @Override
    public BasicResultVO getGroupId(String appName, String title) {
        return null;
    }

    @Override
    public BasicResultVO createGroup(XxlJobGroup xxlJobGroup) {
        return null;
    }



    /**
     * 获取xxl cookie
     *
     * @return String
     */
    private String getCookie() {
        Map<String, Object> params = MapUtil.newHashMap();
        params.put("userName", xxlUserName);
        params.put("password", xxlPassword);
        params.put("randomCode", IdUtil.fastSimpleUUID());

        String path = xxlAddresses + XxlJobConstant.LOGIN_URL;
        HttpResponse response = null;
        try {
            response = HttpRequest.post(path).form(params).execute();
            if (response.isOk()) {
                List<HttpCookie> cookies = response.getCookies();
                StringBuilder sb = new StringBuilder();
                for (HttpCookie cookie : cookies) {
                    sb.append(cookie.toString());
                }
                return sb.toString();
            }
        } catch (Exception e) {
            log.error("CronTaskService#createGroup getCookie,e:{},param:{},response:{}", Throwables.getStackTraceAsString(e)
                    , JSON.toJSONString(params), JSON.toJSONString(response));
        }
        return null;
    }
}
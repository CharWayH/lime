package com.charwayh.esb.support.service;

import com.charwayh.esb.support.vo.MQTemplateParam;

/**
 * @author charwayH
 */
public interface TestService {

    /**
     * 创建MQ脚本
     * @param mqTemplateParam
     */
    void createMQ(MQTemplateParam mqTemplateParam);
}

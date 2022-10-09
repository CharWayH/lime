package com.charwayh.esb.support.service.impl;

import com.charwayh.esb.support.service.TestService;
import com.charwayh.esb.support.utils.MQUtils;
import com.charwayh.esb.support.vo.MQTemplateParam;

/**
 * @author charwayH
 */
public class TestServiceImpl implements TestService {
    @Override
    public void createMQ(MQTemplateParam mqTemplateParam) {

       // MQUtils.createQMGRsh(mqTemplateParam.getSysCode(),"1");

        //MQUtils.createMQsh(mqTemplateParam.getSysCode(), mqTemplateParam.getChannel());

    }
}

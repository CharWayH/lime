package com.charwayh.lime.handler.flowcontrol;

import com.charwayh.lime.common.domain.TaskInfo;

/**
 * @author 3y
 * 流量控制服务
 */
public interface FlowControlService {


    /**
     * 根据渠道进行流量控制
     *
     * @param taskInfo
     * @param flowControlParam
     */
    Double flowControl(TaskInfo taskInfo, FlowControlParam flowControlParam);

}

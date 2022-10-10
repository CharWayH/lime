package com.charwayh.lime.handler.shield;

import com.charwayh.lime.common.domain.TaskInfo;

/**
 * 屏蔽服务
 *
 * @author 3y
 */
public interface ShieldService {


    /**
     * 屏蔽消息
     * @param taskInfo
     */
    void shield(TaskInfo taskInfo);
}

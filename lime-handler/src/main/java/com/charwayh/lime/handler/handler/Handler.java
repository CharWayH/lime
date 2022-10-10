package com.charwayh.lime.handler.handler;

import com.charwayh.lime.common.domain.TaskInfo;
import com.charwayh.lime.support.domain.MessageTemplate;

/**
 * @author 3y
 * 消息处理器
 */
public interface Handler {

    /**
     * 处理器
     *
     * @param taskInfo
     */
    void doHandler(TaskInfo taskInfo);

    /**
     * 撤回消息
     *
     * @param messageTemplate
     * @return
     */
    void recall(MessageTemplate messageTemplate);


}

package com.charwayh.lime.support.mq.eventbus;


import com.charwayh.lime.common.domain.TaskInfo;
import com.charwayh.lime.support.domain.MessageTemplate;

import java.util.List;

/**
 * @author 3y
 * 监听器
 */
public interface EventBusListener {


    /**
     * 消费消息
     * @param lists
     */
    void consume(List<TaskInfo> lists);

    /**
     * 撤回消息
     * @param messageTemplate
     */
    void recall(MessageTemplate messageTemplate);
}

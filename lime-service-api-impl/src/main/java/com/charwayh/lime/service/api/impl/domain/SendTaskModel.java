package com.charwayh.lime.service.api.impl.domain;

import com.charwayh.lime.service.api.domain.MessageParam;
import com.charwayh.lime.support.domain.MessageTemplate;
import com.charwayh.lime.support.pipeline.ProcessModel;
import com.charwayh.lime.common.constant.domain.TaskInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 发送消息任务模型
 * @author charwayH
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SendTaskModel implements ProcessModel {

    /**
     * 消息模板Id
     */
    private Long messageTemplateId;

    /**
     * 请求参数
     */
    private List<MessageParam> messageParamList;

    /**
     * 发送任务的信息
     */
    private List<TaskInfo> taskInfo;

    /**
     * 撤回任务的信息
     */
    private MessageTemplate messageTemplate;

}

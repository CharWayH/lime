package com.charwayh.lime.handler.receiver.eventbus;

import com.google.common.eventbus.Subscribe;
import com.charwayh.lime.common.domain.TaskInfo;
import com.charwayh.lime.handler.receiver.service.ConsumeService;
import com.charwayh.lime.support.constans.MessageQueuePipeline;
import com.charwayh.lime.support.domain.MessageTemplate;
import com.charwayh.lime.support.mq.eventbus.EventBusListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author 3y
 */
@Component
@ConditionalOnProperty(name = "lime.mq.pipeline", havingValue = MessageQueuePipeline.EVENT_BUS)
public class EventBusReceiver implements EventBusListener {

    @Autowired
    private ConsumeService consumeService;

    @Override
    @Subscribe
    public void consume(List<TaskInfo> lists) {
        consumeService.consume2Send(lists);

    }

    @Override
    @Subscribe
    public void recall(MessageTemplate messageTemplate) {
        consumeService.consume2recall(messageTemplate);
    }
}

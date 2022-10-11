package com.charwayh.lime.handler.receiver.kafka;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.fastjson.JSON;
import com.charwayh.lime.common.domain.TaskInfo;
import com.charwayh.lime.handler.receiver.service.ConsumeService;
import com.charwayh.lime.handler.utils.GroupIdMappingUtils;
import com.charwayh.lime.support.constans.MessageQueuePipeline;
import com.charwayh.lime.support.domain.MessageTemplate;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Scope;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * @author 3y
 * 消费MQ的消息
 */
@Slf4j
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@ConditionalOnProperty(name = "lime.mq.pipeline", havingValue = MessageQueuePipeline.KAFKA)
public class Receiver {
    @Autowired
    private ConsumeService consumeService;
    /**
     * 发送消息
     *
     * @param consumerRecord
     * @param topicGroupId
     */
    @KafkaListener(topics = "#{'${lime.business.topic.name}'}", containerFactory = "filterContainerFactory")
    public void consumer(ConsumerRecord<?, String> consumerRecord, @Header(KafkaHeaders.GROUP_ID) String topicGroupId) {
        Optional<String> kafkaMessage = Optional.ofNullable(consumerRecord.value());
        if (kafkaMessage.isPresent()) {

            List<TaskInfo> taskInfoLists = JSON.parseArray(kafkaMessage.get(), TaskInfo.class);
            String messageGroupId = GroupIdMappingUtils.getGroupIdByTaskInfo(CollUtil.getFirst(taskInfoLists.iterator()));
            /**
             * 每个消费者组 只消费 他们自身关心的消息
             */
            if (topicGroupId.equals(messageGroupId)) {
                consumeService.consume2Send(taskInfoLists);
            }
        }
    }

    /**
     * 撤回消息
     * @param consumerRecord
     */
    @KafkaListener(topics = "#{'${lime.business.recall.topic.name}'}",groupId = "#{'${lime.business.recall.group.name}'}",containerFactory = "filterContainerFactory")
    public void recall(ConsumerRecord<?,String> consumerRecord){
        Optional<String> kafkaMessage = Optional.ofNullable(consumerRecord.value());
        if(kafkaMessage.isPresent()){
            MessageTemplate messageTemplate = JSON.parseObject(kafkaMessage.get(), MessageTemplate.class);
            consumeService.consume2recall(messageTemplate);
        }
    }
}

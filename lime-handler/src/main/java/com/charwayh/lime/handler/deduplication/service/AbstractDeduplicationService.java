package com.charwayh.lime.handler.deduplication.service;

import cn.hutool.core.collection.CollUtil;
import com.charwayh.lime.common.domain.AnchorInfo;
import com.charwayh.lime.common.domain.TaskInfo;
import com.charwayh.lime.handler.deduplication.DeduplicationHolder;
import com.charwayh.lime.handler.deduplication.DeduplicationParam;
import com.charwayh.lime.handler.deduplication.limit.LimitService;
import com.charwayh.lime.support.utils.LogUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.Set;

/**
 * @author 3y
 * @date 2021/12/9
 * 去重服务
 */
@Slf4j
public abstract class AbstractDeduplicationService implements DeduplicationService {

    protected Integer deduplicationType;

    protected LimitService limitService;

    @Autowired
    private DeduplicationHolder deduplicationHolder;

    @PostConstruct
    private void init() {
        deduplicationHolder.putService(deduplicationType, this);
    }

    @Autowired
    private LogUtils logUtils;


    @Override
    public void deduplication(DeduplicationParam param) {
        TaskInfo taskInfo = param.getTaskInfo();

        Set<String> filterReceiver = limitService.limitFilter(this, taskInfo, param);

        // 剔除符合去重条件的用户
        if (CollUtil.isNotEmpty(filterReceiver)) {
            taskInfo.getReceiver().removeAll(filterReceiver);
            logUtils.print(AnchorInfo.builder().businessId(taskInfo.getBusinessId()).ids(filterReceiver).state(param.getAnchorState().getCode()).build());
        }
    }


    /**
     * 构建去重的Key
     *
     * @param taskInfo
     * @param receiver
     * @return
     */
    public abstract String deduplicationSingleKey(TaskInfo taskInfo, String receiver);


}

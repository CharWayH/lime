package com.charwayh.lime.handler.deduplication;

import com.charwayh.lime.common.constant.LimeConstant;
import com.charwayh.lime.common.domain.TaskInfo;
import com.charwayh.lime.common.enums.DeduplicationType;
import com.charwayh.lime.support.service.ConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 3y.
 * @date 2021/12/12
 * 去重服务
 */
@Service
public class DeduplicationRuleService {

    public static final String DEDUPLICATION_RULE_KEY = "deduplicationRule";

    @Autowired
    private ConfigService config;

    @Autowired
    private DeduplicationHolder deduplicationHolder;

    public void duplication(TaskInfo taskInfo) {
        // 配置样例：{"deduplication_10":{"num":1,"time":300},"deduplication_20":{"num":5}}
        String deduplicationConfig = config.getProperty(DEDUPLICATION_RULE_KEY, LimeConstant.APOLLO_DEFAULT_VALUE_JSON_OBJECT);

        // 去重
        List<Integer> deduplicationList = DeduplicationType.getDeduplicationList();
        for (Integer deduplicationType : deduplicationList) {
            DeduplicationParam deduplicationParam = deduplicationHolder.selectBuilder(deduplicationType).build(deduplicationConfig, taskInfo);
            if (deduplicationParam != null) {
                deduplicationHolder.selectService(deduplicationType).deduplication(deduplicationParam);
            }
        }
    }


}

package com.charwayh.lime.handler.deduplication.build;

import com.charwayh.lime.common.domain.TaskInfo;
import com.charwayh.lime.common.enums.AnchorState;
import com.charwayh.lime.common.enums.DeduplicationType;
import com.charwayh.lime.handler.deduplication.DeduplicationParam;
import org.springframework.stereotype.Service;


/**
 * @author huskey
 * @date 2022/1/18
 */
@Service
public class ContentDeduplicationBuilder extends AbstractDeduplicationBuilder implements Builder {

    public ContentDeduplicationBuilder() {
        deduplicationType = DeduplicationType.CONTENT.getCode();
    }

    @Override
    public DeduplicationParam build(String deduplication, TaskInfo taskInfo) {
        DeduplicationParam deduplicationParam = getParamsFromConfig(deduplicationType, deduplication, taskInfo);
        if (deduplicationParam == null) {
           return null;
        }
        deduplicationParam.setAnchorState(AnchorState.CONTENT_DEDUPLICATION);
        return deduplicationParam;

    }
}

package com.charwayh.lime.service.api.service;

import com.charwayh.lime.service.api.domain.SendRequest;
import com.charwayh.lime.service.api.domain.SendResponse;

/**
 * @author charwayH
 */
public interface RecallService {


    /**
     * 根据模板ID撤回消息
     *
     * @param sendRequest
     * @return
     */
    SendResponse recall(SendRequest sendRequest);
}


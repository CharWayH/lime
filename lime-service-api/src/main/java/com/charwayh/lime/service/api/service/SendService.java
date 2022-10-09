package com.charwayh.lime.service.api.service;

import com.charwayh.lime.service.api.domain.BatchSendRequest;
import com.charwayh.lime.service.api.domain.SendRequest;
import com.charwayh.lime.service.api.domain.SendResponse;

/**
 * @author charwayH
 * 发送接口
 */
public interface SendService {


    /**
     * 单文案发送接口
     * @param sendRequest
     * @return
     */
    SendResponse send(SendRequest sendRequest);


    /**
     * 多文案发送接口
     * @param batchSendRequest
     * @return
     */
    SendResponse batchSend(BatchSendRequest batchSendRequest);

}

package com.charwayh.lime.handler.flowcontrol.impl;

import com.google.common.util.concurrent.RateLimiter;
import com.charwayh.lime.common.domain.TaskInfo;
import com.charwayh.lime.handler.enums.RateLimitStrategy;
import com.charwayh.lime.handler.flowcontrol.FlowControlParam;
import com.charwayh.lime.handler.flowcontrol.FlowControlService;
import com.charwayh.lime.handler.flowcontrol.annotations.LocalRateLimit;

/**
 * Created by TOM
 * On 2022/7/21 17:05
 */
@LocalRateLimit(rateLimitStrategy = RateLimitStrategy.REQUEST_RATE_LIMIT)
public class RequestRateLimitService implements FlowControlService {

  /**
   * 根据渠道进行流量控制
   *
   * @param taskInfo
   * @param flowControlParam
   */
  @Override
  public Double flowControl(TaskInfo taskInfo, FlowControlParam flowControlParam) {
    RateLimiter rateLimiter = flowControlParam.getRateLimiter();
    return rateLimiter.acquire(1);
  }
}

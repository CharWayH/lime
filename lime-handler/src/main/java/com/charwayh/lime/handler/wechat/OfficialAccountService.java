package com.charwayh.lime.handler.wechat;

import com.charwayh.lime.handler.domain.wechat.WeChatOfficialParam;

import java.util.List;

/**
 * @author zyg
 */
public interface OfficialAccountService {

    /**
     * 发送模板消息
     *
     * @param weChatOfficialParam 模板消息参数
     * @return
     * @throws Exception
     */
    List<String> send(WeChatOfficialParam weChatOfficialParam) throws Exception;

}

package com.charwayh.lime.web.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.charwayh.lime.common.vo.BasicResultVO;
import com.charwayh.lime.support.domain.MessageTemplate;
import com.charwayh.lime.web.vo.MessageTemplateParam;

import java.util.List;

/**
 * 消息模板管理
 *
 * @author charwayH
 *
 */
public interface MessageTemplateService  {

    /**
     * 查询未删除的模板列表（分页)
     *
     * @param messageTemplateParam
     * @return
     */
    List<MessageTemplate> queryList(MessageTemplateParam messageTemplateParam);


    /**
     * 统计未删除的条数
     *
     * @return
     */
    long count();

    /**
     * 单个 保存或者更新
     * 存在ID 更新
     * 不存在ID保存
     *
     * @param messageTemplate
     * @return
     */
    boolean saveOrUpdate(MessageTemplate messageTemplate);


    /**
     * 软删除(deleted=1)
     *
     * @param ids
     */
    void deleteByIds(List<Long> ids);

    /**
     * 根据ID查询模板信息
     * @param id
     * @return
     */
    MessageTemplate queryById(Long id);

    /**
     * 根据ID复制消息模板
     *
     * @param id
     */
    void copy(Long id);

    /**
     * 启动模板的定时任务
     * @param id
     * @return
     */
    BasicResultVO startCronTask(Long id);

    /**
     * 暂停模板的定时任务
     * @param id
     * @return
     */
    BasicResultVO stopCronTask(Long id);


}

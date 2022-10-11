package com.charwayh.lime.web.service.impl;


import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.charwayh.lime.common.constant.LimeConstant;
import com.charwayh.lime.common.enums.AuditStatus;
import com.charwayh.lime.common.enums.MessageStatus;
import com.charwayh.lime.common.enums.RespStatusEnum;
import com.charwayh.lime.common.enums.TemplateType;
import com.charwayh.lime.common.vo.BasicResultVO;
import com.charwayh.lime.cron.xxl.entity.XxlJobInfo;
import com.charwayh.lime.cron.xxl.service.CronTaskService;
import com.charwayh.lime.cron.xxl.utils.XxlJobUtils;
import com.charwayh.lime.support.domain.MessageTemplate;
import com.charwayh.lime.support.mapper.MessageTemplateMapper;
import com.charwayh.lime.web.service.MessageTemplateService;
import com.charwayh.lime.web.vo.MessageTemplateParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author charwayH
 */
@Service
public class MessageTemplateServiceImpl implements MessageTemplateService {

    @Autowired
    private MessageTemplateMapper messageTemplateMapper;

    @Autowired
    private CronTaskService cronTaskService;

    @Autowired
    private XxlJobUtils xxlJobUtils;



    @Override
    public List<MessageTemplate> queryList(MessageTemplateParam param) {
        // 当前页码，每页有几条
        Page<MessageTemplate> page = new Page(param.getPage(), param.getPerPage());
        messageTemplateMapper.selectPage(page,new QueryWrapper<MessageTemplate>().isNotNull("id"));
        ArrayList<MessageTemplate> list = new ArrayList<>();

        list.addAll(page.getRecords());
        return list;
    }

    @Override
    public long count() {
        return messageTemplateMapper.selectCount(null);
    }





    @Override
    public boolean saveOrUpdate(MessageTemplate messageTemplate) {
        // 记录更新时间
        messageTemplate.setUpdated(Math.toIntExact(DateUtil.currentSeconds()));
        int result;
        // 无id则初始化状态(新增)，有id则更新
        if (messageTemplate.getId() == null) {
            initStatus(messageTemplate);
            result = messageTemplateMapper.insert(messageTemplate);
        } else {
            //resetStatus(messageTemplate);
            result = messageTemplateMapper.updateById(messageTemplate);
        }
        if (result == 1) {
            return true;
        } else {
            return false;
        }
    }



    @Override
    public void deleteByIds(List<Long> ids) {
        // 遍历所有要删除的messageTemplate
        for (Long id : ids) {
            MessageTemplate messageTemplate = messageTemplateMapper.selectById(id);
            messageTemplateMapper.deleteById(messageTemplate.getId());

            // 将有定时任务的消息模板的任务删除
            if(messageTemplate.getCronTaskId()!=null && messageTemplate.getCronTaskId() > 0){
                cronTaskService.deleteCronTask(messageTemplate.getCronTaskId());
            }
        }
    }

    @Override
    public MessageTemplate queryById(Long id) {
        return messageTemplateMapper.selectById(id);
    }

    @Override
    public void copy(Long id) {
        // 1.根据id获取消息模板
        MessageTemplate messageTemplate = messageTemplateMapper.selectById(id);
        // 2.复制一个相同的消息模对象(无消息id和定时任务id)
        MessageTemplate clone = ObjectUtil.clone(messageTemplate).setId(null).setCronTaskId(null);
        // 3.存入数据库
        messageTemplateMapper.insert(clone);
    }

    @Override
    public BasicResultVO startCronTask(Long id) {
        // 1.获取消息模板的信息
        MessageTemplate messageTemplate = messageTemplateMapper.selectById(id);
        // 2.动态创建或更新定时任务
        XxlJobInfo xxlJobInfo = xxlJobUtils.buildXxlJobInfo(messageTemplate);
        // 3.获取taskId(如果本身存在则复用原有任务，如果不存在则得到新建后任务ID)
        Integer taskId = messageTemplate.getCronTaskId();
        BasicResultVO basicResultVO = cronTaskService.saveCronTask(xxlJobInfo);
        if(taskId == null && RespStatusEnum.SUCCESS.getCode().equals(basicResultVO.getStatus()) && basicResultVO.getData() != null){
            taskId = Integer.valueOf(String.valueOf(basicResultVO.getData()));
        }
        // 4.启动定时任务
        if(taskId != null){
            cronTaskService.startCronTask(taskId);
            MessageTemplate clone = ObjectUtil.clone(messageTemplate).setMsgStatus(MessageStatus.RUN.getCode()).setCronTaskId(taskId).
                    setUpdated(Math.toIntExact(DateUtil.currentSeconds()));
            saveOrUpdate(clone);
            return BasicResultVO.success();
        }
        return BasicResultVO.fail();
    }

    @Override
    public BasicResultVO stopCronTask(Long id) {
        // 1.修改模板状态
        MessageTemplate messageTemplate = messageTemplateMapper.selectById(id);
        MessageTemplate clone = ObjectUtil.clone(messageTemplate).setMsgStatus(MessageStatus.STOP.getCode()).
                setUpdated(Math.toIntExact(DateUtil.currentSeconds()));
        saveOrUpdate(clone);
        // 2.暂停定时任务
        return cronTaskService.stopCronTask(clone.getCronTaskId());
    }


    /**
     * 初始化状态信息
     * TODO 创建者 修改者 团队
     *
     * @param messageTemplate
     */
    private void initStatus(MessageTemplate messageTemplate) {
        messageTemplate.setFlowId(StrUtil.EMPTY)
                .setMsgStatus(MessageStatus.INIT.getCode()).setAuditStatus(AuditStatus.WAIT_AUDIT.getCode())
                .setCreator("charwayH").setUpdator("charwayH").setTeam("charwayH").setAuditor("charwayH")
                .setCreated(Math.toIntExact(DateUtil.currentSeconds()))
                .setIsDeleted(LimeConstant.FALSE);
    }


    /**
     * 1. 重置模板的状态
     * 2. 修改定时任务信息(如果存在)
     *
     * @param messageTemplate
     */
    private void resetStatus(MessageTemplate messageTemplate) {
        messageTemplate.setUpdator(messageTemplate.getUpdator())
                .setMsgStatus(MessageStatus.INIT.getCode()).setAuditStatus(AuditStatus.WAIT_AUDIT.getCode());

        if (messageTemplate.getCronTaskId() != null && TemplateType.CLOCKING.getCode().equals(messageTemplate.getTemplateType())) {
            XxlJobInfo xxlJobInfo = xxlJobUtils.buildXxlJobInfo(messageTemplate);
            cronTaskService.saveCronTask(xxlJobInfo);
            cronTaskService.stopCronTask(messageTemplate.getCronTaskId());
        }
    }

}

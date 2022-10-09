package com.charwayh.lime.web.controller;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.charwayh.lime.common.enums.RespStatusEnum;
import com.charwayh.lime.common.vo.BasicResultVO;
import com.charwayh.lime.service.api.domain.SendRequest;
import com.charwayh.lime.service.api.domain.SendResponse;
import com.charwayh.lime.service.api.enums.BusinessCode;
import com.charwayh.lime.service.api.service.RecallService;
import com.charwayh.lime.support.domain.MessageTemplate;
import com.charwayh.lime.web.service.MessageTemplateService;
import com.charwayh.lime.web.utils.ConvertMap;
import com.charwayh.lime.web.vo.MessageTemplateParam;
import com.charwayh.lime.web.vo.MessageTemplateVo;
import com.google.common.base.Throwables;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author charwayH
 */

@Slf4j
@RestController
@RequestMapping("/messageTemplate")
@Api(tags = {"消息模板接口"})
@CrossOrigin(origins = "http://localhost:6565", allowCredentials = "true", allowedHeaders = "*")
public class MessageTemplateController {

    @Autowired
    private MessageTemplateService messageTemplateService;

    @Autowired
    private RecallService recallService;

    @Value("${lime.business.upload.crowd.path}")
    private String dataPath;


    @RequestMapping("/test")
    public List<MessageTemplate> test(@RequestBody MessageTemplateParam param){
        return messageTemplateService.queryList(param);
    }

    /**
     * 消息模板列表数据
     */
    @GetMapping("/list")
    @ApiOperation("/列表页")
    public BasicResultVO queryList(MessageTemplateParam messageTemplateParam) {
        List<Map<String, Object>> result = ConvertMap.flatList(messageTemplateService.queryList(messageTemplateParam));
        // 总条数
        long count = messageTemplateService.count();
        // 设置总条数和结果信息
        MessageTemplateVo messageTemplateVo = MessageTemplateVo.builder().count(count).rows(result).build();
        return BasicResultVO.success(messageTemplateVo);
    }

    /**
     * 根据Id删除
     * id多个用逗号分隔开
     */
    @DeleteMapping("delete/{id}")
    @ApiOperation("/根据Ids删除")
    public BasicResultVO deleteByIds(@PathVariable("id") String id) {
        if (StrUtil.isNotBlank(id)) {
            List<Long> idList = Arrays.stream(id.split(StrUtil.COMMA)).map(s -> Long.valueOf(s)).collect(Collectors.toList());
            messageTemplateService.deleteByIds(idList);
            return BasicResultVO.success();
        }
        return BasicResultVO.fail();
    }


    /**
     * 根据Id查找
     */
    @GetMapping("query/{id}")
    @ApiOperation("/根据Id查找")
    public BasicResultVO queryById(@PathVariable("id") Long id) {
        Map<String, Object> result = ConvertMap.flatSingle(messageTemplateService.queryById(id));
        return BasicResultVO.success(result);
    }


    /**
     * 如果Id存在，则修改
     * 如果Id不存在，则保存
     */
    @PostMapping("/save")
    @ApiOperation("/保存数据")
    public BasicResultVO saveOrUpdate(@RequestBody MessageTemplate messageTemplate) {
        MessageTemplate info = messageTemplateService.saveOrUpdate(messageTemplate);
        return BasicResultVO.success(info);
    }

    /**
     * 根据Id复制
     */
    @PostMapping("copy/{id}")
    @ApiOperation("/根据Id复制")
    public BasicResultVO copyById(@PathVariable("id") Long id) {
        messageTemplateService.copy(id);
        return BasicResultVO.success();
    }

    /**
     * 根据Id复制
     */
    @PostMapping("recall/{id}")
    @ApiOperation("/撤回消息接口")
    public BasicResultVO recall(@PathVariable("id") Long id) {
        SendRequest sendRequest = SendRequest.builder().code(BusinessCode.RECALL.getCode()).
                messageTemplateId(Long.valueOf(id)).build();
        SendResponse response = recallService.recall(sendRequest);
        if (response.getCode() != RespStatusEnum.SUCCESS.getCode()) {
            return BasicResultVO.fail(response.getMsg());
        }
        return BasicResultVO.success(response);
    }


    /**
     * 上传人群文件
     */
    @PostMapping("upload")
    @ApiOperation("/上传人群文件")
    public BasicResultVO upload(@RequestParam("file") MultipartFile file) {
        String filePath = new StringBuilder(dataPath)
                .append(IdUtil.fastSimpleUUID())
                .append(file.getOriginalFilename())
                .toString();
        try {
            File localFile = new File(filePath);
            if (!localFile.exists()) {
                localFile.mkdirs();
            }
            //
            file.transferTo(localFile);


        } catch (Exception e) {
            log.error("MessageTemplateController#upload fail! e:{},params{}", Throwables.getStackTraceAsString(e), JSON.toJSONString(file));
            return BasicResultVO.fail(RespStatusEnum.SERVICE_ERROR);
        }
        return BasicResultVO.success(MapUtil.of(new String[][]{{"value", filePath}}));
    }


    @PostMapping("test")
    public String test(@RequestBody MessageTemplate param){
        messageTemplateService.test(param);
        return "成功";
    }
}

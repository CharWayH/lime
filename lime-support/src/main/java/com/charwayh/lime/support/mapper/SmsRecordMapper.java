package com.charwayh.lime.support.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.charwayh.lime.support.domain.SmsRecord;
import org.springframework.stereotype.Repository;


import java.util.List;

/**
 * 短信记录的Dao
 *
 * @author 3y
 */
@Repository
public interface SmsRecordMapper  extends IService<SmsRecord> {

    /**
     * 通过日期和手机号找到发送记录
     *
     * @param phone
     * @param sendDate
     * @return
     */
    List<SmsRecord> findByPhoneAndSendDate(Long phone, Integer sendDate);
}

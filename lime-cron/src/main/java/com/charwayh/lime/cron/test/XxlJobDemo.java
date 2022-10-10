package com.charwayh.lime.cron.test;

import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * @author charwayH
 */
@Component
public class XxlJobDemo {
    @XxlJob("demoJobHandler")
    public void demoJobHandler(){
     //  XxlJobHelper.log("XXL-JOB, Hello World.");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        System.out.println("XXL-JOB, Hello World.-------->" + simpleDateFormat.format(date));
    }
}

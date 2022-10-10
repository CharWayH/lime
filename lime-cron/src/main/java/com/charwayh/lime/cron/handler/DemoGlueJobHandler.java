package com.charwayh.lime.cron.handler;

import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.IJobHandler;

/**
 * @author charwayH
 */
public class DemoGlueJobHandler extends IJobHandler {

    @Override
    public void execute() throws Exception {
        XxlJobHelper.log("XXL-JOB, Hello World.");
        System.out.println("XXL-JOB, Hello World.");
    }

}

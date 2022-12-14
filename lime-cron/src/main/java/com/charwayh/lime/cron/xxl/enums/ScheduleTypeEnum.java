package com.charwayh.lime.cron.xxl.enums;

/**
 * 调度类型
 *
 * @author charwayH
 */
public enum ScheduleTypeEnum {

    NONE,
    /**
     * schedule by cron
     */
    CRON,

    /**
     * schedule by fixed rate (in seconds)
     */
    FIX_RATE;

    ScheduleTypeEnum() {
    }

}

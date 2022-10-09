package com.charwayh.esb.support.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author charwayH
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MQTemplateParam {

    private Long id;

    private String sysCode;

    private String channel;

}

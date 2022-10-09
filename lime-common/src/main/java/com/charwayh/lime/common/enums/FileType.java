package com.charwayh.lime.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * @author charwayH
 * 文件类型
 */
@Getter
@ToString
@AllArgsConstructor
public enum FileType {
    IMAGE("10", "image"),
    VOICE("20", "voice"),
    COMMON_FILE("30", "file"),
    VIDEO("40", "video"),
    ;
    private String code;
    private String dingDingName;

    public static String dingDingNameByCode(String code) {
        for (FileType fileType : FileType.values()) {
            if (fileType.getCode().equals(code)) {
                return fileType.getDingDingName();
            }
        }
        return null;
    }
}

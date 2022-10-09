package com.charwayh.lime.support.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;


/**
 * @author charwayH
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class User {
    @TableId(type = IdType.AUTO)
    private Long id;

    private String username;

    private String password;
}

package com.doubleSelection.common.core.domain.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户注册对象
 * 
 * @author ruoyi
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterBody extends LoginBody
{
    private Long roleId;//角色类型 100导师 101学生
    private String name;
    private String email;
    private String major;
    private String interests;
    private String researchArea;
    private Long studentLimit;
}

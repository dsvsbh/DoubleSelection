package com.doubleSelection.doubleSelection.domain;


import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.doubleSelection.common.annotation.Excel;
import com.doubleSelection.common.core.domain.BaseEntity;

import java.util.Date;

/**
 * 学生对象 student
 * 
 * @author liuzhicheng
 * @date 2024-09-23
 */
@Data
public class Student
{
    private static final long serialVersionUID = 1L;

    /** 学生ID */
    private Long studentId;

    /** 学生姓名 */
    @Excel(name = "学生姓名")
    private String name;

    /** 学生邮箱 */
    @Excel(name = "学生邮箱")
    private String email;

    /** 学生密码 */
    private String password;

    /** 专业 */
    @Excel(name = "专业")
    private String major;

    /** 研究兴趣 */
    @Excel(name = "研究兴趣")
    private String interests;
    private Date createTime;
    private Date updateTime;

    public void setStudentId(Long studentId) 
    {
        this.studentId = studentId;
    }

    public Long getStudentId() 
    {
        return studentId;
    }
    public void setName(String name) 
    {
        this.name = name;
    }

    public String getName() 
    {
        return name;
    }
    public void setEmail(String email) 
    {
        this.email = email;
    }

    public String getEmail() 
    {
        return email;
    }
    public void setPassword(String password) 
    {
        this.password = password;
    }

    public String getPassword() 
    {
        return password;
    }
    public void setMajor(String major) 
    {
        this.major = major;
    }

    public String getMajor() 
    {
        return major;
    }
    public void setInterests(String interests) 
    {
        this.interests = interests;
    }

    public String getInterests() 
    {
        return interests;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("studentId", getStudentId())
            .append("name", getName())
            .append("email", getEmail())
            .append("password", getPassword())
            .append("major", getMajor())
            .append("interests", getInterests())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .toString();
    }


}
